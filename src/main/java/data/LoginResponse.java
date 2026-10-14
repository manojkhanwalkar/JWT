/*
 * LoginResponse
 *
 * Reply from POST /login: on success, a session id. On failure, status "failed" and an
 * error message.
 */
package data;

public class LoginResponse {

    /** Login outcome. The "sucess" spelling is kept for wire compatibility. */
    public enum Status { sucess, failed }

    private String sessionId;

    private String errorMessage = "";

    private Status status;

    /** Creates a successful response that carries the new session id. */
    public LoginResponse(String sessionId) {
        this.sessionId = sessionId;
        this.status = Status.sucess;
    }

    /** No-arg constructor for Jackson. */
    public LoginResponse() {
    }

    /** Session id to present to the authorizer. Null on failure. */
    public String getSessionId() {
        return sessionId;
    }

    /** Sets the session id. */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /** Error description. Empty on success. */
    public String getErrorMessage() {
        return errorMessage;
    }

    /** Sets the error message and marks the response as failed. */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.status = Status.failed;
    }

    /** Returns the login outcome. */
    public Status getStatus() {
        return status;
    }

    /** Sets the login outcome. */
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LoginResponse{sessionId='" + sessionId + "', errorMessage='" + errorMessage + "', status=" + status + '}';
    }
}
