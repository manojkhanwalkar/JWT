/*
 * CryptoRoundTripTest
 *
 * Tests for the ECDHE and JSON utilities: two DHUtil sides derive the same key,
 * KeyExchangeManager plus the client logic do a full encrypt/decrypt round trip,
 * failures throw instead of leaking plaintext, and JSONUtil round-trips DTOs.
 */
package util;

import data.EncryptedMessage;
import data.KeyExchangeRequest;
import data.KeyExchangeResponse;
import data.LoginResponse;
import org.junit.Test;

import java.security.KeyPair;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CryptoRoundTripTest {

    /** Both peers derive the same AES key, so each side can read the other's messages. */
    @Test
    public void bothSidesDeriveSameKey() {
        KeyPair a = CryptUtil.generateECKeyPair();
        KeyPair b = CryptUtil.generateECKeyPair();
        DHUtil alice = new DHUtil(a.getPublic(), a.getPrivate());
        DHUtil bob = new DHUtil(b.getPublic(), b.getPrivate());
        alice.setReceiverPublicKey(b.getPublic());
        bob.setReceiverPublicKey(a.getPublic());

        byte[] iv = DHUtil.newIv();
        String cipher = alice.encrypt("héllo wörld", iv);
        assertNotEquals("héllo wörld", cipher);
        assertEquals("héllo wörld", bob.decrypt(cipher, Base64.getEncoder().encodeToString(iv)));
    }

    /** Same message flow as Connection.esend and AppResource /eservice, without HTTP. */
    @Test
    public void serverKeyExchangeRoundTrip() {
        KeyPair client = CryptUtil.generateECKeyPair();
        KeyExchangeResponse resp = new KeyExchangeManager().processExchange(
                new KeyExchangeRequest(CryptUtil.convertPublicKeyToString(client.getPublic())));

        DHUtil util = new DHUtil(client.getPublic(), client.getPrivate());
        util.setReceiverPublicKey(CryptUtil.convertStringtoPublicKey(resp.getPublicKey(), "EC"));

        byte[] iv = DHUtil.newIv();
        EncryptedMessage req = new EncryptedMessage();
        req.setSessionId(resp.getSessionId());
        req.setMessage(util.encrypt("ping", iv));
        req.setIv(Base64.getEncoder().encodeToString(iv));

        assertEquals("ping", KeyExchangeManager.decryptRequest(req));
        EncryptedMessage reply = KeyExchangeManager.encryptResponse("PONG", req);
        assertEquals("PONG", util.decrypt(reply.getMessage(), reply.getIv()));
    }

    /** Encrypting before the key exchange throws. It used to return the plaintext. */
    @Test(expected = IllegalStateException.class)
    public void encryptWithoutKeyExchangeFails() {
        KeyPair a = CryptUtil.generateECKeyPair();
        new DHUtil(a.getPublic(), a.getPrivate()).encrypt("secret", DHUtil.newIv());
    }

    /** JSONUtil serializes and parses DTOs, and its output has no polymorphic type metadata. */
    @Test
    public void jsonRoundTrip() {
        String json = JSONUtil.toJSON(new LoginResponse("abc"));
        assertTrue(json, json.startsWith("{"));
        assertEquals("abc", JSONUtil.fromJSON(json, LoginResponse.class).getSessionId());
    }
}
