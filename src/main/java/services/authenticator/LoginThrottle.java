/*
 * LoginThrottle
 *
 * Brute-force protection for POST /login. It counts login attempts per user id
 * (reserved before the password check, cleared on success);
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

    private static final int MAX_TRACKED = 100_000;

    private final int maxFailures;

    /** Attempts per user id in the current window: failures plus logins still in progress. */
    private final Cache<String, AtomicInteger> attempts;

    /**
     * Locked user ids. Kept apart from {@link #attempts} so that flooding the counter
     * cache with random user ids cannot evict (and so unlock) an account: filling this
     * cache needs MAX_FAILURES failures per entry.
     */
    private final Cache<String, Boolean> locked;

    public LoginThrottle() {
        this(MAX_FAILURES, LOCKOUT);
    }

    LoginThrottle(int maxFailures, Duration window) {
        this.maxFailures = maxFailures;
        this.attempts = CacheBuilder.newBuilder().expireAfterWrite(window).maximumSize(MAX_TRACKED).build();
        this.locked = CacheBuilder.newBuilder().expireAfterWrite(window).maximumSize(MAX_TRACKED).build();
    }

    /** Returns true if {@code userid} has too many recent failures to try again. */
    public boolean isLocked(String userid) {
        return locked.getIfPresent(key(userid)) != null;
    }

    /**
     * Reserves one login attempt for {@code userid} before the password is checked.
     * The reservation is atomic, so concurrent attempts cannot all slip past the
     * limit while the slow password check runs. Returns false if the user is locked.
     * A reserved attempt counts as a failure unless {@link #recordSuccess} clears it.
     */
    public boolean tryAcquire(String userid) {
        String key = key(userid);
        if (locked.getIfPresent(key) != null) {
            return false;
        }
        int count;
        try {
            count = attempts.get(key, AtomicInteger::new).incrementAndGet();
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
        if (count >= maxFailures) {
            locked.put(key, Boolean.TRUE);
        }
        return count <= maxFailures;
    }

    /** Clears the attempt count and any lock after a successful login. */
    public void recordSuccess(String userid) {
        attempts.invalidate(key(userid));
        locked.invalidate(key(userid));
    }

    private static String key(String userid) {
        return userid == null ? "" : userid;
    }
}
