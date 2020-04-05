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


    public LoginResponse()
    {

    }


}
