/*
 * AppResponse
 *
 * Reply from POST /service: whether the JWT was valid and, if it was, the upper-cased
 * echo of the request payload.
 */
package data;

public class AppResponse {

    /** Outcome of the JWT check. */
    public enum Status { Valid, Invalid }

    private Status status;

    /** Upper-cased echo of the payload. The "dumyecho" spelling is kept for wire compatibility. */
    private String dumyecho;

    /** No-arg constructor for Jackson. */
    public AppResponse() {
    }

    /** Creates a populated response. */
    public AppResponse(Status status, String dumyecho) {
        this.status = status;
        this.dumyecho = dumyecho;
    }

    /** Returns whether the token was valid. */
    public Status getStatus() {
        return status;
    }

    /** Sets the token status. */
    public void setStatus(Status status) {
        this.status = status;
    }

    /** Returns the upper-cased payload, or "" if the token was invalid. */
    public String getDumyecho() {
        return dumyecho;
    }

    /** Sets the echoed payload. */
    public void setDumyecho(String dumyecho) {
        this.dumyecho = dumyecho;
    }

    @Override
    public String toString() {
        return "AppResponse{status=" + status + ", dumyecho='" + dumyecho + "'}";
    }
}
