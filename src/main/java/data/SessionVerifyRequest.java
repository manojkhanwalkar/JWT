package data;

public class SessionVerifyRequest {

    String sessionId;

    public SessionVerifyRequest(String sessionId) {
        this.sessionId = sessionId;
    }


    public SessionVerifyRequest() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
