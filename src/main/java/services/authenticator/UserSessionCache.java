/*
 * UserSessionCache
 *
 * Process-wide (singleton) store of live login sessions. It is used by the
 * authenticator service:
 *   - login puts userid -> sessionId (one active session per user; a new login
 *     invalidates the previous one)
 *   - verify checks whether a sessionId is still live
 *   - logout removes the user's session
 * A session expires SESSION_TTL after login.
 *
 * Expiry is handled by Guava's Cache. It is checked on every read, so an expired
 * session is never reported as valid, even between clean-up passes. A daemon thread
 * calls cleanUp() each second, which frees memory and logs expirations.
 */
package services.authenticator;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class UserSessionCache {

    /** How long a session lasts after login. */
    static final Duration SESSION_TTL = Duration.ofSeconds(5);

    private static final class Holder {
        static final UserSessionCache CACHE = new UserSessionCache(SESSION_TTL);
    }

    /** sessionId -> userid, with automatic expiry. */
    private final Cache<String, String> sessions;

    /** userid -> current sessionId. Used to find the session to drop on logout or re-login. */
    private final ConcurrentMap<String, String> userSessions = new ConcurrentHashMap<>();

    /** Returns the shared, process-wide instance. */
    public static UserSessionCache getInstance() {
        return Holder.CACHE;
    }

    /** Creates a cache whose sessions last {@code ttl}. Package-private so tests can use short TTLs. */
    UserSessionCache(Duration ttl) {
        sessions = CacheBuilder.newBuilder()
                .expireAfterWrite(ttl)
                .<String, String>removalListener(n -> {
                    if (n.getCause() == RemovalCause.EXPIRED) {
                        userSessions.remove(n.getValue(), n.getKey());
                        System.out.println("Session expired for user " + n.getValue());
                    }
                })
                .build();

        ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "session-evictor");
            t.setDaemon(true);
            return t;
        });
        cleaner.scheduleAtFixedRate(sessions::cleanUp, 1, 1, TimeUnit.SECONDS);
    }

    /** Returns true if {@code sessionId} belongs to a live, unexpired session. */
    public boolean contains(String sessionId) {
        return sessionId != null && sessions.getIfPresent(sessionId) != null;
    }

    /** Records a new session for {@code userid}. Any earlier session for that user is invalidated. */
    public synchronized void put(String userid, String sessionId) {
        String previous = userSessions.put(userid, sessionId);
        if (previous != null) {
            sessions.invalidate(previous);
        }
        sessions.put(sessionId, userid);
    }

    /** Ends {@code userid}'s session, if they have one. */
    public synchronized void remove(String userid) {
        String sessionId = userSessions.remove(userid);
        if (sessionId != null) {
            sessions.invalidate(sessionId);
        }
    }

    /** Demo: creates ten sessions at random intervals and lets them expire. */
    public static void main(String[] args) throws Exception {
        UserSessionCache cache = new UserSessionCache(SESSION_TTL);

        for (int i = 0; i < 10; i++) {
            cache.put("User " + i, UUID.randomUUID().toString());
            Thread.sleep(ThreadLocalRandom.current().nextInt(10) * 1000L);
        }

        Thread.sleep(SESSION_TTL.toMillis() + 2000); // let the remaining sessions expire
    }
}
