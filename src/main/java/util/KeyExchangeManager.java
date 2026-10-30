/*
 * KeyExchangeManager
 *
 * Server side of the ECDHE ("ephemeral" ECDH) message-encryption flow:
 *   1. /keyexchange: the client sends its EC public key. We create a fresh key pair,
 *      derive the shared AES key, store it under a new session id, and return our
 *      public key and the session id.
 *   2. /eservice: the client sends a message encrypted under that session. We decrypt
 *      it, and then encrypt the reply under the same key, which ends the session.
 *
 * A new KeyExchangeManager (and so a new key pair) is made for every exchange,
 * which gives forward secrecy.
 */
package util;

import data.EncryptedMessage;
import data.KeyExchangeRequest;
import data.KeyExchangeResponse;

import java.security.KeyPair;
import java.util.Base64;
import java.util.UUID;

public class KeyExchangeManager {

    private static final SessionKeyCache sessionKeyCache = new SessionKeyCache();

    private final KeyPair keyPair;

    /** Generates the ephemeral server key pair for a single key exchange. */
    public KeyExchangeManager() {
        keyPair = CryptUtil.generateECKeyPair();
    }

    /**
     * Completes the key agreement with the client's public key and registers the
     * resulting session.
     *
     * @return our public key and the session id the client must quote in /eservice
     */
    public KeyExchangeResponse processExchange(KeyExchangeRequest request) {
        DHUtil util = new DHUtil(keyPair.getPublic(), keyPair.getPrivate());
        util.setReceiverPublicKey(CryptUtil.convertStringtoPublicKey(request.getPublicKey(), "EC"));

        String sessionId = UUID.randomUUID().toString();
        sessionKeyCache.put(sessionId, util);

        return new KeyExchangeResponse(CryptUtil.convertPublicKeyToString(keyPair.getPublic()), sessionId);
    }

    /**
     * Decrypts a client message with the key negotiated for its session id. If the
     * message does not decrypt (wrong key, tampered data), the session is dropped so
     * it cannot be probed again.
     *
     * @throws IllegalArgumentException if the session is unknown or has expired
     * @throws IllegalStateException    if decryption fails
     */
    public static String decryptRequest(EncryptedMessage request) {
        DHUtil util = sessionKeyCache.get(request.getSessionId());
        if (util == null) {
            throw new IllegalArgumentException("Unknown or expired session");
        }
        try {
            return util.decrypt(request.getMessage(), request.getIv());
        } catch (RuntimeException e) {
            sessionKeyCache.use(request.getSessionId());
            throw e;
        }
    }

    /**
     * Encrypts the response under the request's session key with a fresh IV, and
     * then removes the session.
     *
     * @throws IllegalArgumentException if the session is unknown or has expired
     */
    public static EncryptedMessage encryptResponse(String response, EncryptedMessage request) {
        DHUtil util = sessionKeyCache.use(request.getSessionId());
        if (util == null) {
            throw new IllegalArgumentException("Unknown or expired session");
        }
        byte[] iv = DHUtil.newIv();

        EncryptedMessage message = new EncryptedMessage();
        message.setMessage(util.encrypt(response, iv));
        message.setIv(Base64.getEncoder().encodeToString(iv));
        return message;
    }
}
