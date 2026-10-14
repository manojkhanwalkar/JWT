/*
 * LogoutRequest
 *
 * Body of POST /logout on the authenticator. It names the user whose session should end.
 */
package data;

public class LogoutRequest {

    private String userid;

    private String sessionId;

    /** No-arg constructor for Jackson. */
    public LogoutRequest() {
    }

    /** Creates a logout request. */
    public LogoutRequest(String userid, String sessionId) {
        this.userid = userid;
        this.sessionId = sessionId;
    }

    /** User whose session is ended. */
    public String getUserid() {
        return userid;
    }

    /** Sets the user id. */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /** The session id. Informational only; logout goes by userid. */
    public String getSessionId() {
        return sessionId;
    }

    /** Sets the session id. */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
