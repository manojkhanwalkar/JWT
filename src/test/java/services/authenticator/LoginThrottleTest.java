/*
 * LoginThrottleTest
 *
 * Brute-force lockout and the session-bound logout in UserSessionCache.
 */
package services.authenticator;

import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginThrottleTest {

    @Test
    public void locksAfterMaxFailuresAndResetsOnSuccess() {
        LoginThrottle throttle = new LoginThrottle(3, Duration.ofMinutes(1));
        for (int i = 0; i < 3; i++) {
            assertFalse(throttle.isLocked("u"));
            assertTrue(throttle.tryAcquire("u"));
        }
        assertTrue(throttle.isLocked("u"));
        assertFalse(throttle.tryAcquire("u"));
        assertFalse("other users unaffected", throttle.isLocked("v"));
        throttle.recordSuccess("u");
        assertFalse(throttle.isLocked("u"));
    }

    /** Concurrent attempts reserve their slot up front, so no more than the limit get through. */
    @Test
    public void concurrentAttemptsCannotExceedLimit() throws Exception {
        LoginThrottle throttle = new LoginThrottle(5, Duration.ofMinutes(1));
        java.util.concurrent.atomic.AtomicInteger allowed = new java.util.concurrent.atomic.AtomicInteger();
        java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newFixedThreadPool(16);
        java.util.concurrent.CountDownLatch go = new java.util.concurrent.CountDownLatch(1);
        for (int i = 0; i < 200; i++) {
            pool.submit(() -> {
                go.await();
                if (throttle.tryAcquire("victim")) {
                    allowed.incrementAndGet();
                }
                return null;
            });
        }
        go.countDown();
        pool.shutdown();
        assertTrue(pool.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS));
        assertEquals(5, allowed.get());
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
