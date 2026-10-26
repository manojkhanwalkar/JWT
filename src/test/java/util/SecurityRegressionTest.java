/*
 * SecurityRegressionTest
 *
 * Regression tests for the OWASP audit fixes: JWT claim checks, AES-GCM integrity,
 * single-use key-exchange sessions after a failed decryption, and password hashing.
 */
package util;

import data.EncryptedMessage;
import data.KeyExchangeRequest;
import data.KeyExchangeResponse;
import dynamodb.PasswordHasher;
import org.junit.Test;

import java.security.KeyPair;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SecurityRegressionTest {

    private final KeyPair keys = CryptUtil.generateECKeyPair();
    private final JWSSignUtil signer = new JWSSignUtil(keys.getPrivate(), keys.getPublic());
    private final JWSVerifyUtil verifier = new JWSVerifyUtil(keys.getPublic());

    @Test
    public void issuedTokenVerifies() {
        assertTrue(verifier.verifyJWS(signer.issueToken("user1")));
    }

    /** A signed payload without exp/iss/aud/sub (the old "Dummy Token") is no longer accepted. */
    @Test
    public void claimlessTokenRejected() {
        assertFalse(verifier.verifyJWS(signer.jwsSign("Dummy Token")));
    }

    @Test
    public void tamperedTokenRejected() {
        String[] parts = signer.issueToken("user1").split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1])).replace("user1", "admin");
        String forged = parts[0] + "." + Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes()) + "." + parts[2];
        assertFalse(verifier.verifyJWS(forged));
        assertFalse(verifier.verifyJWS("garbage"));
        assertFalse(verifier.verifyJWS(null));
    }

    /** Flipping one ciphertext bit is detected by the GCM tag. */
    @Test
    public void tamperedCiphertextRejected() {
        KeyPair a = CryptUtil.generateECKeyPair();
        KeyPair b = CryptUtil.generateECKeyPair();
        DHUtil alice = new DHUtil(a.getPublic(), a.getPrivate());
        DHUtil bob = new DHUtil(b.getPublic(), b.getPrivate());
        alice.setReceiverPublicKey(b.getPublic());
        bob.setReceiverPublicKey(a.getPublic());
        byte[] iv = DHUtil.newIv();
        byte[] ct = Base64.getDecoder().decode(alice.encrypt("pay 10", iv));
        ct[0] ^= 1;
        try {
            bob.decrypt(Base64.getEncoder().encodeToString(ct), Base64.getEncoder().encodeToString(iv));
            fail("tampered ciphertext accepted");
        } catch (IllegalStateException expected) {
        }
    }

    /** After a failed decryption the session is dropped, so it cannot be used as a decryption oracle. */
    @Test
    public void failedDecryptionEndsSession() {
        KeyPair client = CryptUtil.generateECKeyPair();
        KeyExchangeResponse resp = new KeyExchangeManager().processExchange(
                new KeyExchangeRequest(CryptUtil.convertPublicKeyToString(client.getPublic())));
        EncryptedMessage bad = new EncryptedMessage();
        bad.setSessionId(resp.getSessionId());
        bad.setMessage(Base64.getEncoder().encodeToString(new byte[32]));
        bad.setIv(Base64.getEncoder().encodeToString(DHUtil.newIv()));
        try {
            KeyExchangeManager.decryptRequest(bad);
            fail("garbage decrypted");
        } catch (IllegalStateException expected) {
        }
        try {
            KeyExchangeManager.decryptRequest(bad);
            fail("session survived a failed decryption");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void passwordHashing() {
        String stored = PasswordHasher.hash("password1");
        assertNotEquals("password1", stored);
        assertTrue(PasswordHasher.verify("password1", stored));
        assertFalse(PasswordHasher.verify("password2", stored));
        assertFalse("legacy plaintext must not validate", PasswordHasher.verify("password1", "password1"));
        assertFalse(PasswordHasher.verify("password1", null));
        assertEquals(4, stored.split("\\$").length);
    }
}
