/*
 * LoginThrottle
 *
 * Brute-force protection for POST /login. It counts failed logins per user id;
 * after MAX_FAILURES failures within LOCKOUT the account is locked until the window
 * expires (counted from the first failure). A successful login clears the counter.
 *
 * Trade-off: an attacker who knows a user id can keep that account locked. For a
 * real deployment, add per-client-IP limits and alerting in front of this.
 */
package services.authenticator;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginThrottle {

    static final int MAX_FAILURES = 5;

    static final Duration LOCKOUT = Duration.ofMinutes(15);

    private final int maxFailures;

    private final Cache<String, AtomicInteger> failures;

    public LoginThrottle() {
        this(MAX_FAILURES, LOCKOUT);
    }

    LoginThrottle(int maxFailures, Duration window) {
        this.maxFailures = maxFailures;
        this.failures = CacheBuilder.newBuilder()
                .expireAfterWrite(window)
                .maximumSize(100_000)
                .build();
    }

    /** Returns true if {@code userid} has too many recent failures to try again. */
    public boolean isLocked(String userid) {
        AtomicInteger count = failures.getIfPresent(key(userid));
        return count != null && count.get() >= maxFailures;
    }

    /** Records a failed login for {@code userid}. */
    public void recordFailure(String userid) {
        try {
            failures.get(key(userid), AtomicInteger::new).incrementAndGet();
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Clears the failure count after a successful login. */
    public void recordSuccess(String userid) {
        failures.invalidate(key(userid));
    }

    private static String key(String userid) {
        return userid == null ? "" : userid;
    }
}
