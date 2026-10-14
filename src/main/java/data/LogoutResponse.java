/*
 * LogoutResponse
 *
 * Reply from POST /logout.
 */
package data;

public class LogoutResponse {

    /** Logout outcome. The "sucess" spelling is kept for wire compatibility. */
    public enum Status { sucess, failed }

    private String errorMessage = "";

    private Status status;

    /** Creates a response with the given outcome. */
    public LogoutResponse(Status status) {
        this.status = status;
    }

    /** No-arg constructor for Jackson. */
    public LogoutResponse() {
    }

    /**
     * Returns the logout outcome. Before these getters existed, Jackson could not
     * serialize this class, so /logout always failed with HTTP 500.
     */
    public Status getStatus() {
        return status;
    }

    /** Sets the logout outcome. */
    public void setStatus(Status status) {
        this.status = status;
    }

    /** Error description. Empty on success. */
    public String getErrorMessage() {
        return errorMessage;
    }

    /** Sets the error description. */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
