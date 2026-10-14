/*
 * TokenRequest
 *
 * Body of POST /token on the authorizer: a login session id to trade for a JWT.
 */
package data;

public class TokenRequest {

    private String sessionId;

    /** Creates a token request for {@code sessionId}. */
    public TokenRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    /** No-arg constructor for Jackson. */
    public TokenRequest() {
    }

    /** Session id returned by /login. */
    public String getSessionId() {
        return sessionId;
    }

    /** Sets the session id. */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
