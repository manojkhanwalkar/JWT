/*
 * LoginResourceTest
 *
 * POST /login over the per-IP rate limit answers 429 with Retry-After before any
 * credential check.
 */
package services.authenticator;

import data.LoginRequest;
import data.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LoginResourceTest {

    /** A request whose only implemented method is getRemoteAddr. */
    private static HttpServletRequest from(String ip) {
        return (HttpServletRequest) Proxy.newProxyInstance(HttpServletRequest.class.getClassLoader(),
                new Class<?>[] {HttpServletRequest.class}, (proxy, method, args) -> {
                    if (method.getName().equals("getRemoteAddr")) {
                        return ip;
                    }
                    throw new UnsupportedOperationException(method.getName());
                });
    }

    @Test
    public void overLimitGets429WithRetryAfter() {
        // Capacity 0: every attempt is over the limit. The manager is never reached, so it is null.
        LoginResource resource = new LoginResource(null, new LoginRateLimiter(0, Duration.ofSeconds(6), System::nanoTime));
        try {
            resource.login(new LoginRequest("user1", "password1"), from("10.0.0.1"));
            fail("expected 429");
        } catch (WebApplicationException e) {
            Response response = e.getResponse();
            assertEquals(429, response.getStatus());
            assertEquals("6", response.getHeaderString("Retry-After"));
            LoginResponse body = (LoginResponse) response.getEntity();
            assertEquals(LoginResponse.Status.failed, body.getStatus());
            assertTrue(body.getErrorMessage().startsWith("Too many login attempts"));
        }
    }
}
