/*
 * KeyExchangeResponse
 *
 * Reply from POST /keyexchange: the server's ephemeral EC public key and the session id
 * that the client must send with its encrypted message.
 */
package data;

public class KeyExchangeResponse {

    private String publicKey;

    private String sessionId;

    /** Creates a populated response. */
    public KeyExchangeResponse(String publicKey, String sessionId) {
        this.publicKey = publicKey;
        this.sessionId = sessionId;
    }

    /** No-arg constructor for Jackson. */
    public KeyExchangeResponse() {
    }

    /** Server's Base64 X.509 EC public key. */
    public String getPublicKey() {
        return publicKey;
    }

    /** Sets the server's public key. */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /** Session id under which the derived key is stored on the server. */
    public String getSessionId() {
        return sessionId;
    }

    /** Sets the session id. */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
