package data;

public class TokenRequest {

    String sessionId;

    public TokenRequest(String sessionId) {
        this.sessionId = sessionId;
    }


    public TokenRequest() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
