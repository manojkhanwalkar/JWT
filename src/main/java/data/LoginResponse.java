package data;

public class LoginResponse {

    enum Status { sucess , failed};

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


}
