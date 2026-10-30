/*
 * LoginRateLimitFilterTest
 *
 * The /login filter passes requests within the limit to Jersey and answers the rest
 * with 429, Retry-After and a failed LoginResponse, without calling the chain.
 */
package services.authenticator;

import data.LoginResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import util.JSONUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginRateLimitFilterTest {

    private final LoginRateLimitFilter filter = new LoginRateLimitFilter(new LoginRateLimiter(
            new LoginRateLimiter.Limit(1, Duration.ofSeconds(6)),
            new LoginRateLimiter.Limit(10, Duration.ofSeconds(6)),
            () -> 0L));

    private final AtomicInteger chainCalls = new AtomicInteger();

    private final FilterChain chain = (req, res) -> chainCalls.incrementAndGet();

    /** Records status, headers and body written through the HttpServletResponse methods the filter uses. */
    private final Map<String, Object> written = new HashMap<>();

    private final StringWriter body = new StringWriter();

    private final HttpServletResponse response = (HttpServletResponse) Proxy.newProxyInstance(
            HttpServletResponse.class.getClassLoader(), new Class<?>[] {HttpServletResponse.class},
            (proxy, method, args) -> switch (method.getName()) {
                case "setStatus" -> written.put("status", args[0]);
                case "setHeader" -> written.put(args[0].toString(), args[1]);
                case "setContentType", "setCharacterEncoding" -> null;
                case "getWriter" -> new PrintWriter(body, true);
                default -> throw new UnsupportedOperationException(method.getName());
            });

    private static ServletRequest from(String ip) {
        return (ServletRequest) Proxy.newProxyInstance(ServletRequest.class.getClassLoader(),
                new Class<?>[] {ServletRequest.class}, (proxy, method, args) -> {
                    if (method.getName().equals("getRemoteAddr")) {
                        return ip;
                    }
                    throw new UnsupportedOperationException(method.getName());
                });
    }

    @Test
    public void withinLimitReachesResourceThenOverLimitGets429() throws Exception {
        filter.doFilter(from("10.0.0.1"), response, chain);
        assertEquals(1, chainCalls.get());
        assertTrue(written.isEmpty());

        filter.doFilter(from("10.0.0.1"), response, chain);
        assertEquals("over the limit, the resource is never called", 1, chainCalls.get());
        assertEquals(429, written.get("status"));
        assertEquals("6", written.get("Retry-After"));
        LoginResponse reply = JSONUtil.fromJSON(body.toString(), LoginResponse.class);
        assertEquals(LoginResponse.Status.failed, reply.getStatus());
        assertTrue(reply.getErrorMessage().startsWith("Too many login attempts"));
    }
}
