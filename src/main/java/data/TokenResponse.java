package data;

public class TokenResponse {

    public enum Status { Valid , Invalid};

    Status status;
    String jwt;

    public TokenResponse(Status status, String jwt) {
        this.status = status;
        this.jwt = jwt;
    }


    public TokenResponse() {
    }

    @Override
    public String toString() {
        return "SessionVerifyResponse{" +
                "status=" + status +
                ", jwt='" + jwt + '\'' +
                '}';
    }
}
