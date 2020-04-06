package data;

public class SessionVerifyResponse {

    public enum Status { Valid , Invalid};

    Status status;
    String sessionId;

    public SessionVerifyResponse(Status status, String sessionId) {
        this.status = status;
        this.sessionId = sessionId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public SessionVerifyResponse() {
    }

    @Override
    public String toString() {
        return "SessionVerifyResponse{" +
                "status=" + status +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
