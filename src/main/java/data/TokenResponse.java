/*
 * TokenResponse
 *
 * Reply from POST /token: if the session was valid, an ES256-signed JWT. If not,
 * status Invalid and a random placeholder.
 */
package data;

public class TokenResponse {

    /** Whether a real token was issued. */
    public enum Status { Valid, Invalid }

    private Status status;

    private String jwt;

    /** Creates a populated response. */
    public TokenResponse(Status status, String jwt) {
        this.status = status;
        this.jwt = jwt;
    }

    /** No-arg constructor for Jackson. */
    public TokenResponse() {
    }

    /** Returns whether a real token was issued. */
    public Status getStatus() {
        return status;
    }

    /** Sets the token status. */
    public void setStatus(Status status) {
        this.status = status;
    }

    /** Compact-serialized JWS. Only meaningful when the status is Valid. */
    public String getJwt() {
        return jwt;
    }

    /** Sets the token. */
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String toString() {
        return "TokenResponse{status=" + status + ", jwt='" + jwt + "'}";
    }
}
