/*
 * DHUtil
 *
 * One side of an Elliptic-Curve Diffie-Hellman (ECDH) session. It is built from
 * our own key pair. When the peer's public key arrives it derives the shared secret,
 * hashes that secret into an AES-256 session key, and then encrypts and decrypts
 * messages with AES-GCM (authenticated encryption: tampered ciphertext is rejected).
 *
 * The client (util.Connection#esend) and the server (util.KeyExchangeManager) both
 * use this class. Because both sides run the same derivation, they end up with the
 * same session key.
 */
package util;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class DHUtil {

    private static final String CIPHER = "AES/GCM/NoPadding";

    /** Recommended GCM nonce length (96 bits). A fresh random one is used for every message. */
    public static final int IV_LENGTH = 12;

    /** GCM authentication tag length in bits. */
    private static final int TAG_BITS = 128;

    private final PublicKey publickey;
    private final PrivateKey privateKey;

    private Key sessionKey;

    /** Creates a session from our own EC key pair. Call {@link #setReceiverPublicKey} before encrypting. */
    public DHUtil(PublicKey publicKey, PrivateKey privateKey) {
        this.publickey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * Finishes the ECDH agreement with the peer's public key and derives the AES
     * session key.
     *
     * @throws IllegalArgumentException if the peer key is not a usable EC key
     */
    public void setReceiverPublicKey(PublicKey otherPublicKey) {
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(otherPublicKey, true);
            byte[] sharedSecret = keyAgreement.generateSecret();
            sessionKey = new SecretKeySpec(deriveKey(sharedSecret, publickey, otherPublicKey), "AES");
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("ECDH key agreement failed", e);
        }
    }

    /**
     * Encrypts a UTF-8 message with the session key and the given IV.
     *
     * @return Base64-encoded ciphertext
     * @throws IllegalStateException if encryption fails. Before this change, a failure
     *         returned the plaintext without any error, which leaked it onto the wire.
     */
    public String encrypt(String msg, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, requireSessionKey(), gcmSpec(iv));
            byte[] encrypted = cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Encryption failed", e);
        }
    }

    /**
     * Decrypts Base64 ciphertext produced by {@link #encrypt} on the peer's side.
     *
     * @param iv Base64-encoded IV that was sent with the message
     * @throws IllegalStateException if decryption fails, for example because of a wrong key or tampered data
     */
    public String decrypt(String encryptedData, String iv) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, requireSessionKey(), gcmSpec(Base64.getDecoder().decode(iv)));
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException | NullPointerException e) {
            throw new IllegalStateException("Decryption failed", e);
        }
    }

    /** Returns our side's public key for this session. */
    public PublicKey getPublickey() {
        return publickey;
    }

    /** Returns a fresh random IV for {@link #encrypt}. */
    public static byte[] newIv() {
        byte[] iv = new byte[IV_LENGTH];
        CryptUtil.SECURE_RANDOM.nextBytes(iv);
        return iv;
    }

    private static GCMParameterSpec gcmSpec(byte[] iv) {
        if (iv == null || iv.length != IV_LENGTH) {
            throw new IllegalArgumentException("IV must be " + IV_LENGTH + " bytes");
        }
        return new GCMParameterSpec(TAG_BITS, iv);
    }

    private Key requireSessionKey() {
        if (sessionKey == null) {
            throw new IllegalStateException("Key exchange not completed: call setReceiverPublicKey first");
        }
        return sessionKey;
    }

    /**
     * Hashes the raw shared secret together with both public keys, using SHA-256, to
     * get a 256-bit AES key. The two public keys are sorted first, so both sides
     * produce the same key no matter which side is "ours". Mixing in the public keys
     * means different key pairs that happen to share a secret still get different keys.
     */
    private static byte[] deriveKey(byte[] sharedSecret, PublicKey ours, PublicKey theirs)
            throws GeneralSecurityException {
        MessageDigest hash = MessageDigest.getInstance("SHA-256");
        hash.update(sharedSecret);
        ByteBuffer a = ByteBuffer.wrap(ours.getEncoded());
        ByteBuffer b = ByteBuffer.wrap(theirs.getEncoded());
        if (a.compareTo(b) <= 0) {
            hash.update(a);
            hash.update(b);
        } else {
            hash.update(b);
            hash.update(a);
        }
        return hash.digest();
    }
}
