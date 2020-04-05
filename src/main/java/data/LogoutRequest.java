package data;

public class LogoutRequest {


    public LogoutRequest()
    {

    }

    public LogoutRequest(String userid, String sessionId) {
        this.userid = userid;
        this.sessionId = sessionId;
    }




    String userid;

    String sessionId;


}
