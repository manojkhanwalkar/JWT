package data;

public class LogoutRequest {


    public LogoutRequest()
    {

    }

    public LogoutRequest(String userid, String sessionId) {
        this.userid = userid;
        this.sessionId = sessionId;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    String userid;

    String sessionId;


}
