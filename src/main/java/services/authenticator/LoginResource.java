/*
 * LoginResource
 *
 * REST endpoints of the authenticator service:
 *   POST /login   check credentials and get a session id (rate limited per client
 *                 IP; over the limit it returns 429 with Retry-After)
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    private static final Logger LOG = LoggerFactory.getLogger(LoginResource.class);

    private final LoginManager loginManager;

    private final LoginRateLimiter rateLimiter;

    public LoginResource() {
        this(new LoginManager(), new LoginRateLimiter());
    }

    LoginResource(LoginManager loginManager, LoginRateLimiter rateLimiter) {
        this.loginManager = loginManager;
        this.rateLimiter = rateLimiter;
    }

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

    /**
     * Checks the credentials and returns a new session id, or an error. A client over
     * its rate limit gets 429 before any password work is done. The client is the
     * TCP peer address: forwarded headers are off (Authenticator.yml), so a client
     * cannot pick its own key with X-Forwarded-For.
     */
    @POST
    @Timed
    @Path("/login")
    public LoginResponse login(LoginRequest request, @Context HttpServletRequest http) {
        String clientIp = http.getRemoteAddr();
        Duration wait = rateLimiter.tryAcquire(clientIp);
        if (!wait.isZero()) {
            LOG.warn("Login rate limit exceeded for {}", clientIp);
            LoginResponse response = new LoginResponse();
            response.setErrorMessage("Too many login attempts, try again later");
            throw new WebApplicationException(Response.status(Response.Status.TOO_MANY_REQUESTS)
                    .header("Retry-After", Math.max(1, (wait.toMillis() + 999) / 1000))
                    .type(MediaType.APPLICATION_JSON)
                    .entity(response)
                    .build());
        }
        return loginManager.login(request);
    }
}
