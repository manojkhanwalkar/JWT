package data;

public class LoginResponse {

    public enum Status { sucess , failed};

    String sessionId ;

    String errorMessage="";


    Status status ;

    public LoginResponse(String sessionId) {
        this.sessionId = sessionId;
        status=Status.sucess;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.status  = Status.failed;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LoginResponse()
    {

    }


    @Override
    public String toString() {
        return "LoginResponse{" +
                "sessionId='" + sessionId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", status=" + status +
                '}';
    }
}
