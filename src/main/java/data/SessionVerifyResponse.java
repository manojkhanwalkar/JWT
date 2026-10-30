/*
 * SessionVerifyResponse
 *
 * Reply from POST /verify: whether the session id is live.
 */
package data;

public class SessionVerifyResponse {

    /** Session status. */
    public enum Status { Valid, Invalid }

    private Status status;

    private String sessionId;

    /** Owner of the session; set only when the status is Valid. */
    private String userid;

    /** Creates a populated response. */
    public SessionVerifyResponse(Status status, String sessionId) {
        this.status = status;
        this.sessionId = sessionId;
    }

    /** No-arg constructor for Jackson. */
    public SessionVerifyResponse() {
    }

    /** Returns whether the session is live. */
    public Status getStatus() {
        return status;
    }

    /** Sets the session status. */
    public void setStatus(Status status) {
        this.status = status;
    }

    /** Session id that was checked. */
    public String getSessionId() {
        return sessionId;
    }

    /** Sets the session id. */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /** User id that owns the session, or null if the session is not valid. */
    public String getUserid() {
        return userid;
    }

    /** Sets the owning user id. */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "SessionVerifyResponse{status=" + status + ", sessionId='" + sessionId + "', userid='" + userid + "'}";
    }
}
