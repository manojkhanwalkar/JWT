/*
 * LoginThrottleTest
 *
 * Brute-force lockout and the session-bound logout in UserSessionCache.
 */
package services.authenticator;

import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginThrottleTest {

    @Test
    public void locksAfterMaxFailuresAndResetsOnSuccess() {
        LoginThrottle throttle = new LoginThrottle(3, Duration.ofMinutes(1));
        for (int i = 0; i < 3; i++) {
            assertFalse(throttle.isLocked("u"));
            throttle.recordFailure("u");
        }
        assertTrue(throttle.isLocked("u"));
        assertFalse("other users unaffected", throttle.isLocked("v"));
        throttle.recordSuccess("u");
        assertFalse(throttle.isLocked("u"));
    }

    /** Knowing only a user id (or a wrong session id) does not log that user out. */
    @Test
    public void logoutRequiresOwnSession() {
        UserSessionCache cache = new UserSessionCache(Duration.ofMinutes(1));
        cache.put("alice", "s1");
        assertFalse(cache.remove("alice", "wrong"));
        assertFalse(cache.remove("alice", null));
        assertTrue(cache.contains("s1"));
        assertTrue(cache.remove("alice", "s1"));
        assertFalse(cache.contains("s1"));
    }
}
