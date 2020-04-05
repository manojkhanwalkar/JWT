package data;

public class LogoutResponse {

    public enum Status { sucess , failed};



    String errorMessage="";


    Status status ;

    public LogoutResponse(Status status) {

        this.status = status;

    }


    public LogoutResponse()
    {

    }


}
