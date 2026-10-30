/*
 * LoginRateLimiter
 *
 * Per-client-IP rate limit for POST /login, applied before any password hashing.
 * LoginThrottle limits guesses per user id; this limits how fast one client can
 * try anything at all, which stops password spraying across many user ids, CPU
 * exhaustion through PBKDF2, and an attacker locking out many accounts at once.
 *
 * Token buckets, two tiers:
 *   - per client: CLIENT.capacity attempts in a burst, refilled at one per
 *     CLIENT.refillInterval (10 per minute). IPv6 clients are keyed by /64,
 *     since one host usually controls a whole /64.
 *   - per IPv6 /48: a shared, larger bucket (IPV6_48), because a single customer
 *     is often given a /48 or /56 and could otherwise rotate through its /64s.
 * An attempt must pass both.
 *
 * Buckets are kept in bounded caches. Pushing a bucket out of a full cache needs
 * MAX_TRACKED distinct keys; rotating IPv6 addresses inside one /48 does not get
 * past the /48 tier, so that takes about as many real networks as it would take
 * to beat a per-IP limit anyway.
 */
package services.authenticator;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.net.InetAddresses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogText;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.time.Duration;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

public class LoginRateLimiter {

    private static final Logger LOG = LoggerFactory.getLogger(LoginRateLimiter.class);

    /** A bucket's size and how often one attempt is added back. */
    record Limit(int capacity, Duration refillInterval) {
    }

    static final Limit CLIENT = new Limit(10, Duration.ofSeconds(6));

    static final Limit IPV6_48 = new Limit(100, Duration.ofMillis(600));

    private static final int MAX_TRACKED = 100_000;

    private final Tier clients;

    private final Tier ipv6Networks;

    private final LongSupplier clock;

    public LoginRateLimiter() {
        this(CLIENT, IPV6_48, System::nanoTime);
    }

    LoginRateLimiter(Limit client, Limit ipv6Network, LongSupplier clock) {
        this.clients = new Tier(client);
        this.ipv6Networks = new Tier(ipv6Network);
        this.clock = clock;
    }

    /**
     * Takes one attempt for {@code clientIp}. Returns {@link Duration#ZERO} if the
     * attempt is allowed, otherwise how long the client must wait.
     */
    public Duration tryAcquire(String clientIp) {
        InetAddress address = parse(clientIp);
        Duration wait = clients.take(clientKey(clientIp, address));
        if (wait.isZero() && address instanceof Inet6Address) {
            wait = ipv6Networks.take(prefix(address, 6) + "::/48");
        }
        return wait;
    }

    /** The per-client key: an IPv4 address as-is, an IPv6 address by its /64. */
    static String clientKey(String clientIp) {
        return clientKey(clientIp, parse(clientIp));
    }

    private static String clientKey(String clientIp, InetAddress address) {
        if (address == null) {
            return clientIp == null ? "" : clientIp;
        }
        return address instanceof Inet6Address ? prefix(address, 8) + "::/64" : InetAddresses.toAddrString(address);
    }

    /** Parses an IP literal (IPv6 with or without brackets). Never does a DNS lookup; null if not a literal. */
    private static InetAddress parse(String clientIp) {
        if (clientIp == null) {
            return null;
        }
        String literal = clientIp.startsWith("[") && clientIp.endsWith("]")
                ? clientIp.substring(1, clientIp.length() - 1)
                : clientIp;
        try {
            return InetAddresses.forString(literal);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String prefix(InetAddress address, int bytes) {
        return HexFormat.of().formatHex(Arrays.copyOf(address.getAddress(), bytes));
    }

    /** One bucket per key. Buckets idle long enough to refill fully expire, which loses nothing. */
    private final class Tier {

        private final Limit limit;

        private final long refillNanos;

        private final Cache<String, Bucket> buckets;

        Tier(Limit limit) {
            this.limit = limit;
            this.refillNanos = limit.refillInterval().toNanos();
            this.buckets = CacheBuilder.newBuilder()
                    .expireAfterAccess(refillNanos * limit.capacity(), TimeUnit.NANOSECONDS)
                    .maximumSize(MAX_TRACKED)
                    .build();
        }

        Duration take(String key) {
            Bucket bucket;
            try {
                bucket = buckets.get(key, () -> new Bucket(this));
            } catch (ExecutionException e) {
                throw new IllegalStateException(e);
            }
            return bucket.take(key);
        }
    }

    private final class Bucket {

        private final Tier tier;

        /** Available attempts, scaled by refillNanos so refilling stays in integer math. */
        private long scaledTokens;

        private long lastRefill;

        /** Set on the first rejection, so a client hammering the limit is logged once, not per request. */
        private boolean exhausted;

        Bucket(Tier tier) {
            this.tier = tier;
            this.scaledTokens = tier.limit.capacity() * tier.refillNanos;
            this.lastRefill = clock.getAsLong();
        }

        synchronized Duration take(String key) {
            // Read the clock under the lock so concurrent requests are applied in time order.
            long now = clock.getAsLong();
            long max = tier.limit.capacity() * tier.refillNanos;
            scaledTokens = Math.min(max, scaledTokens + Math.max(0, now - lastRefill));
            lastRefill = Math.max(lastRefill, now);
            if (scaledTokens >= tier.refillNanos) {
                scaledTokens -= tier.refillNanos;
                exhausted = false;
                return Duration.ZERO;
            }
            if (!exhausted) {
                exhausted = true;
                LOG.warn("Login rate limit reached for {}", LogText.sanitize(key));
            }
            return Duration.ofNanos(tier.refillNanos - scaledTokens);
        }
    }
}
