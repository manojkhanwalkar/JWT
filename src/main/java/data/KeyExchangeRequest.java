/*
 * KeyExchangeRequest
 *
 * Body of POST /keyexchange: the client's ephemeral EC public key (Base64 X.509).
 */
package data;

public class KeyExchangeRequest {

    private String publicKey;

    /** Creates a request that carries the client's public key. */
    public KeyExchangeRequest(String publicKey) {
        this.publicKey = publicKey;
    }

    /** No-arg constructor for Jackson. */
    public KeyExchangeRequest() {
    }

    /** Client's Base64 X.509 EC public key. */
    public String getPublicKey() {
        return publicKey;
    }

    /** Sets the client's public key. */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
