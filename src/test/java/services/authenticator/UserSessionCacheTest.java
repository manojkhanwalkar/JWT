/*
 * UserSessionCacheTest
 *
 * Tests UserSessionCache: sessions expire, logout invalidates them, and a re-login
 * invalidates the previous session without expiring the new one.
 */
package services.authenticator;

import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserSessionCacheTest {

    /** A session is valid at first and becomes invalid after the TTL. */
    @Test
    public void sessionExpires() throws Exception {
        UserSessionCache cache = new UserSessionCache(Duration.ofMillis(200));
        cache.put("u", "s1");
        assertTrue(cache.contains("s1"));
        Thread.sleep(400);
        assertFalse(cache.contains("s1"));
    }

    /** Logout invalidates the session right away. */
    @Test
    public void logoutInvalidates() {
        UserSessionCache cache = new UserSessionCache(Duration.ofMinutes(1));
        cache.put("u", "s1");
        cache.remove("u");
        assertFalse(cache.contains("s1"));
    }

    /**
     * Regression test: before the fix, logging in again left the old session valid
     * forever, and its expiry removed the new session.
     */
    @Test
    public void reloginReplacesOldSession() throws Exception {
        UserSessionCache cache = new UserSessionCache(Duration.ofMillis(300));
        cache.put("u", "old");
        Thread.sleep(150);
        cache.put("u", "new");
        assertFalse(cache.contains("old"));
        Thread.sleep(200); // past "old"'s original expiry, but not "new"'s
        assertTrue(cache.contains("new"));
    }
}
