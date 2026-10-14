/*
 * AppRequest
 *
 * Body of POST /service on the app service: a JWT plus a sample payload that the
 * service echoes back in upper case if the token is valid.
 */
package data;

public class AppRequest {

    private String token;

    private String dummy;

    /** Creates a request with a JWT and a payload. */
    public AppRequest(String token, String dummy) {
        this.token = token;
        this.dummy = dummy;
    }

    /** No-arg constructor for Jackson. */
    public AppRequest() {
    }

    /** Compact-serialized JWT issued by the authorizer. */
    public String getToken() {
        return token;
    }

    /** Sets the JWT. */
    public void setToken(String token) {
        this.token = token;
    }

    /** Sample payload to be processed. */
    public String getDummy() {
        return dummy;
    }

    /** Sets the sample payload. */
    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
}
