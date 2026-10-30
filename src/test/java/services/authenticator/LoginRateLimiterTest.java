/*
 * LoginRateLimiterTest
 *
 * Per-IP token buckets for POST /login, driven by a fake clock.
 */
package services.authenticator;

import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class LoginRateLimiterTest {

    private final AtomicLong now = new AtomicLong();

    private final LoginRateLimiter limiter = new LoginRateLimiter(
            new LoginRateLimiter.Limit(3, Duration.ofSeconds(10)),
            new LoginRateLimiter.Limit(5, Duration.ofSeconds(10)),
            now::get);

    @Test
    public void allowsBurstThenReportsWait() {
        for (int i = 0; i < 3; i++) {
            assertEquals(Duration.ZERO, limiter.tryAcquire("10.0.0.1"));
        }
        assertEquals(Duration.ofSeconds(10), limiter.tryAcquire("10.0.0.1"));

        now.addAndGet(Duration.ofSeconds(4).toNanos());
        assertEquals(Duration.ofSeconds(6), limiter.tryAcquire("10.0.0.1"));
    }

    @Test
    public void refillsOverTimeUpToCapacity() {
        for (int i = 0; i < 3; i++) {
            limiter.tryAcquire("10.0.0.1");
        }
        now.addAndGet(Duration.ofSeconds(10).toNanos());
        assertEquals(Duration.ZERO, limiter.tryAcquire("10.0.0.1"));
        assertTrue(limiter.tryAcquire("10.0.0.1").compareTo(Duration.ZERO) > 0);

        // A long idle period refills to capacity, not beyond it.
        now.addAndGet(Duration.ofHours(1).toNanos());
        for (int i = 0; i < 3; i++) {
            assertEquals(Duration.ZERO, limiter.tryAcquire("10.0.0.1"));
        }
        assertTrue(limiter.tryAcquire("10.0.0.1").compareTo(Duration.ZERO) > 0);
    }

    /** A clock reading older than the last one (a request that lost the race) never removes tokens. */
    @Test
    public void outOfOrderClockDoesNotDrainBucket() {
        now.set(Duration.ofSeconds(100).toNanos());
        assertEquals(Duration.ZERO, limiter.tryAcquire("10.0.0.1"));
        now.set(Duration.ofSeconds(90).toNanos());
        assertEquals(Duration.ZERO, limiter.tryAcquire("10.0.0.1"));
        assertEquals(Duration.ZERO, limiter.tryAcquire("10.0.0.1"));
        assertEquals(Duration.ofSeconds(10), limiter.tryAcquire("10.0.0.1"));
    }

    @Test
    public void clientsHaveSeparateBuckets() {
        for (int i = 0; i < 3; i++) {
            limiter.tryAcquire("10.0.0.1");
        }
        assertNotEquals(Duration.ZERO, limiter.tryAcquire("10.0.0.1"));
        assertEquals(Duration.ZERO, limiter.tryAcquire("10.0.0.2"));
    }

    /** One host usually owns a whole IPv6 /64, so rotating the low bits does not get a new bucket. */
    @Test
    public void ipv6SharesClientBucketPerSlash64() {
        assertEquals(LoginRateLimiter.clientKey("2001:db8:1:2::1"), LoginRateLimiter.clientKey("2001:db8:1:2:ffff::9"));
        assertNotEquals(LoginRateLimiter.clientKey("2001:db8:1:2::1"), LoginRateLimiter.clientKey("2001:db8:1:3::1"));
        assertEquals("10.0.0.1", LoginRateLimiter.clientKey("10.0.0.1"));
        // Jetty reports IPv6 peers in brackets.
        assertEquals(LoginRateLimiter.clientKey("2001:db8:1:2::1"), LoginRateLimiter.clientKey("[2001:db8:1:2::7]"));
    }

    /** Rotating through /64s inside one /48 runs into the shared /48 bucket. */
    @Test
    public void ipv6Slash48IsLimitedAsAWhole() {
        int allowed = 0;
        for (int subnet = 0; subnet < 20; subnet++) {
            if (limiter.tryAcquire("2001:db8:1:" + Integer.toHexString(subnet) + "::1").isZero()) {
                allowed++;
            }
        }
        assertEquals(5, allowed);
        assertEquals(Duration.ZERO, limiter.tryAcquire("2001:db8:2::1"));
    }

    /** A value that is not an IP literal is used as an opaque key, never resolved. */
    @Test
    public void nonLiteralIsAnOpaqueKey() {
        assertEquals("a:b.attacker.example", LoginRateLimiter.clientKey("a:b.attacker.example"));
        assertEquals(Duration.ZERO, limiter.tryAcquire("a:b.attacker.example"));
    }
}
