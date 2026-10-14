/*
 * EncryptedMessage
 *
 * Envelope for AES-encrypted traffic on /eservice. It carries the Base64 ciphertext,
 * the Base64 IV, and (on requests) the session id from the key exchange that
 * identifies which session key to use.
 */
package data;

public class EncryptedMessage {

    private String message;

    private String sessionId;

    private String iv;

    /** Base64 AES/CBC ciphertext. */
    public String getMessage() {
        return message;
    }

    /** Sets the ciphertext. */
    public void setMessage(String message) {
        this.message = message;
    }

    /** Key-exchange session id. Set on requests only. */
    public String getSessionId() {
        return sessionId;
    }

    /** Sets the session id. */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /** Base64 16-byte IV used to encrypt {@link #getMessage()}. */
    public String getIv() {
        return iv;
    }

    /** Sets the IV. */
    public void setIv(String iv) {
        this.iv = iv;
    }
}
