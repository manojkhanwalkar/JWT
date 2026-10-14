/*
 * LoginManager
 *
 * Business logic of the authenticator service. It checks credentials (through
 * UserManager/DynamoDB), creates and ends sessions, and answers "is this session
 * still valid?" for the authorizer.
 */
package services.authenticator;

import data.LoginRequest;
import data.LoginResponse;
import data.LogoutRequest;
import data.LogoutResponse;
import data.SessionVerifyRequest;
import data.SessionVerifyResponse;

import java.util.UUID;

public class LoginManager {

    private final UserSessionCache cache = UserSessionCache.getInstance();

    private final UserManager manager = UserManager.getInstance();

    /**
     * Checks the user id and password. On success it starts a new session and returns
     * its id. On failure it returns a response with status "failed".
     */
    public LoginResponse login(LoginRequest request) {
        if (manager.validate(request.getUserid(), request.getPassword())) {
            String sessionId = UUID.randomUUID().toString();
            cache.put(request.getUserid(), sessionId);
            return new LoginResponse(sessionId);
        }
        LoginResponse response = new LoginResponse();
        response.setErrorMessage("User login failed");
        return response;
    }

    /** Ends the user's current session. */
    public LogoutResponse logout(LogoutRequest request) {
        cache.remove(request.getUserid());
        return new LogoutResponse(LogoutResponse.Status.sucess);
    }

    /** Reports whether the given session id is live (logged in and not expired). */
    public SessionVerifyResponse verify(SessionVerifyRequest request) {
        boolean valid = cache.contains(request.getSessionId());
        return new SessionVerifyResponse(valid ? SessionVerifyResponse.Status.Valid : SessionVerifyResponse.Status.Invalid,
                request.getSessionId());
    }

    /** Demo: logs in, checks the session, waits past the TTL, and checks it again. Needs local DynamoDB. */
    public static void main(String[] args) throws Exception {
        LoginManager loginManager = new LoginManager();

        LoginResponse response = loginManager.login(new LoginRequest("user1", "password1"));
        System.out.println(response);

        if (response.getStatus() == LoginResponse.Status.sucess) {
            SessionVerifyRequest vrequest = new SessionVerifyRequest(response.getSessionId());
            System.out.println(loginManager.verify(vrequest));

            Thread.sleep(UserSessionCache.SESSION_TTL.toMillis() * 2);

            System.out.println(loginManager.verify(vrequest));
        }
    }
}
