/*
 * LoginResource
 *
 * REST endpoints of the authenticator service:
 *   POST /login   check credentials and get a session id
 *   POST /verify  check whether a session id is still valid (called by the authorizer)
 *   POST /logout  end a user's session
 */
package services.authenticator;

import com.codahale.metrics.annotation.Timed;
import data.LoginRequest;
import data.LoginResponse;
import data.LogoutRequest;
import data.LogoutResponse;
import data.SessionVerifyRequest;
import data.SessionVerifyResponse;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    private final LoginManager loginManager = new LoginManager();

    /** Ends the user's session. */
    @POST
    @Timed
    @Path("/logout")
    public LogoutResponse logout(LogoutRequest request) {
        return loginManager.logout(request);
    }

    /** Reports whether the session id is still valid. */
    @POST
    @Timed
    @Path("/verify")
    public SessionVerifyResponse verify(SessionVerifyRequest request) {
        return loginManager.verify(request);
    }

    /** Checks the credentials and returns a new session id, or an error. */
    @POST
    @Timed
    @Path("/login")
    public LoginResponse login(LoginRequest request) {
        return loginManager.login(request);
    }
}
