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
import dynamodb.PasswordHasher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class LoginManager {

    private static final Logger LOG = LoggerFactory.getLogger(LoginManager.class);

    private final UserSessionCache cache = UserSessionCache.getInstance();

    private final LoginThrottle throttle = new LoginThrottle();

    private final UserManager manager = UserManager.getInstance();

    /**
     * Checks the user id and password. On success it starts a new session and returns
     * its id. On failure it returns a response with status "failed". After too many
     * failures the user id is locked out for a while (see LoginThrottle); a locked
     * account gets the same generic failure response.
     */
    public LoginResponse login(LoginRequest request) {
        String userid = request.getUserid();
        if (!throttle.tryAcquire(userid)) {
            // Same hashing work as a real check, so response time does not reveal the lock.
            PasswordHasher.dummyVerify(request.getPassword());
            LOG.warn("Login rejected for locked-out user '{}'", sanitize(userid));
        } else if (manager.validate(userid, request.getPassword())) {
            throttle.recordSuccess(userid);
            String sessionId = UUID.randomUUID().toString();
            cache.put(userid, sessionId);
            return new LoginResponse(sessionId);
        } else {
            LOG.warn("Failed login for user '{}'", sanitize(userid));
        }
        LoginResponse response = new LoginResponse();
        response.setErrorMessage("User login failed");
        return response;
    }

    /**
     * Ends the user's current session. The caller must present that session id;
     * a user id alone is not enough, so one user cannot log out another.
     */
    public LogoutResponse logout(LogoutRequest request) {
        boolean ended = cache.remove(request.getUserid(), request.getSessionId());
        return new LogoutResponse(ended ? LogoutResponse.Status.sucess : LogoutResponse.Status.failed);
    }

    /** Reports whether the given session id is live (logged in and not expired). */
    public SessionVerifyResponse verify(SessionVerifyRequest request) {
        String userid = cache.userFor(request.getSessionId());
        SessionVerifyResponse response = new SessionVerifyResponse(
                userid != null ? SessionVerifyResponse.Status.Valid : SessionVerifyResponse.Status.Invalid,
                request.getSessionId());
        response.setUserid(userid);
        return response;
    }

    /** Strips CR/LF and caps the length so a user id cannot forge log lines. */
    private static String sanitize(String value) {
        if (value == null) {
            return "null";
        }
        String clean = value.replaceAll("[\\r\\n\\t]", "_");
        return clean.length() > 64 ? clean.substring(0, 64) + "..." : clean;
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
