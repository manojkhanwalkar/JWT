/*
 * LoginRateLimiter
 *
 * Per-client-IP rate limit for POST /login, applied before any password hashing.
 * LoginThrottle limits guesses per user id; this limits how fast one client can
 * try anything at all, which stops password spraying across many user ids, CPU
 * exhaustion through PBKDF2, and an attacker locking out many accounts at once.
 *
 * Each client gets a token bucket: CAPACITY attempts in a burst, refilled at one
 * attempt per REFILL_INTERVAL (10 per minute by default). IPv6 clients are keyed
 * by their /64 prefix, since one host usually controls a whole /64.
 */
package services.authenticator;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

public class LoginRateLimiter {

    static final int CAPACITY = 10;

    static final Duration REFILL_INTERVAL = Duration.ofSeconds(6);

    private static final int MAX_TRACKED = 100_000;

    private final int capacity;

    private final long refillNanos;

    private final LongSupplier clock;

    /**
     * Buckets idle long enough to refill completely expire, which loses nothing: a
     * new bucket is full too. Size eviction needs MAX_TRACKED distinct clients.
     */
    private final Cache<String, Bucket> buckets;

    public LoginRateLimiter() {
        this(CAPACITY, REFILL_INTERVAL, System::nanoTime);
    }

    LoginRateLimiter(int capacity, Duration refillInterval, LongSupplier clock) {
        this.capacity = capacity;
        this.refillNanos = refillInterval.toNanos();
        this.clock = clock;
        this.buckets = CacheBuilder.newBuilder()
                .expireAfterAccess(refillNanos * capacity, TimeUnit.NANOSECONDS)
                .maximumSize(MAX_TRACKED)
                .build();
    }

    /**
     * Takes one attempt from {@code clientIp}'s bucket. Returns {@link Duration#ZERO}
     * if the attempt is allowed, otherwise how long the client must wait.
     */
    public Duration tryAcquire(String clientIp) {
        Bucket bucket;
        try {
            bucket = buckets.get(key(clientIp), () -> new Bucket(capacity, clock.getAsLong()));
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
        return bucket.take(clock.getAsLong());
    }

    /** IPv4 addresses as-is, IPv6 addresses by /64 prefix. */
    static String key(String clientIp) {
        if (clientIp == null) {
            return "";
        }
        if (clientIp.indexOf(':') < 0) {
            return clientIp;
        }
        try {
            // A string with ':' is parsed as an IPv6 literal, never looked up in DNS.
            byte[] address = InetAddress.getByName(clientIp).getAddress();
            return address.length == 16
                    ? HexFormat.of().formatHex(Arrays.copyOf(address, 8)) + "::/64"
                    : clientIp;
        } catch (UnknownHostException e) {
            return clientIp;
        }
    }

    private final class Bucket {

        /** Available attempts, scaled by refillNanos so refilling stays in integer math. */
        private long scaledTokens;

        private long lastRefill;

        Bucket(int tokens, long now) {
            this.scaledTokens = tokens * refillNanos;
            this.lastRefill = now;
        }

        synchronized Duration take(long now) {
            long max = capacity * refillNanos;
            scaledTokens = Math.min(max, scaledTokens + (now - lastRefill));
            lastRefill = now;
            if (scaledTokens >= refillNanos) {
                scaledTokens -= refillNanos;
                return Duration.ZERO;
            }
            return Duration.ofNanos(refillNanos - scaledTokens);
        }
    }
}
