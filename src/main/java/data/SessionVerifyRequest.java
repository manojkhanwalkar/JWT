/*
 * SessionVerifyRequest
 *
 * Body of POST /verify on the authenticator. The authorizer sends it to check a
 * session id before it issues a JWT.
 */
package data;

public class SessionVerifyRequest {

    private String sessionId;

    /** Creates a request to check {@code sessionId}. */
    public SessionVerifyRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    /** No-arg constructor for Jackson. */
    public SessionVerifyRequest() {
    }

    /** Session id to check. */
    public String getSessionId() {
        return sessionId;
    }

    /** Sets the session id. */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
