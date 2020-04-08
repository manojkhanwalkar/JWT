package data;

public class KeyExchangeResponse {



   String publicKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    public KeyExchangeResponse(String publicKey, String sessionId) {
        this.publicKey = publicKey;
        this.sessionId = sessionId;
    }

    public KeyExchangeResponse() {
    }
}
