package data;

public class SessionVerifyResponse {

    public enum Status { Valid , Invalid};

    Status status;
    String sessionId;

    public SessionVerifyResponse(Status status, String sessionId) {
        this.status = status;
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
