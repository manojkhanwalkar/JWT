package data;

public class KeyExchangeRequest {



   String publicKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public KeyExchangeRequest(String publicKey) {
        this.publicKey = publicKey;
    }

    public KeyExchangeRequest() {
    }
}
