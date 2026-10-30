/*
 * LoginRateLimitFilter
 *
 * Servlet filter on /login that applies LoginRateLimiter before Jersey runs, so a
 * request over the limit is rejected without its body being read or parsed. The
 * reply is 429 with Retry-After and the usual failed LoginResponse JSON.
 *
 * The client is the TCP peer address: forwarded headers are off (Authenticator.yml),
 * so a client cannot pick its own key with X-Forwarded-For.
 */
package services.authenticator;

import data.LoginResponse;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import util.JSONUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class LoginRateLimitFilter implements Filter {

    static final int TOO_MANY_REQUESTS = 429;

    private final LoginRateLimiter limiter;

    public LoginRateLimitFilter() {
        this(new LoginRateLimiter());
    }

    LoginRateLimitFilter(LoginRateLimiter limiter) {
        this.limiter = limiter;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Duration wait = limiter.tryAcquire(request.getRemoteAddr());
        if (wait.isZero()) {
            chain.doFilter(request, response);
            return;
        }
        LoginResponse body = new LoginResponse();
        body.setErrorMessage("Too many login attempts, try again later");
        HttpServletResponse http = (HttpServletResponse) response;
        http.setStatus(TOO_MANY_REQUESTS);
        http.setHeader("Retry-After", Long.toString(Math.max(1, (wait.toMillis() + 999) / 1000)));
        http.setContentType("application/json");
        http.setCharacterEncoding(StandardCharsets.UTF_8.name());
        http.getWriter().write(JSONUtil.toJSON(body));
    }
}
