/*
 * SessionKeyCache
 *
 * Server-side store that maps a key-exchange session id to its DHUtil (the derived
 * AES session key). An entry is created by /keyexchange and used once by /eservice.
 *
 * Entries expire after a fixed TTL. Before this, a client that did the key exchange
 * but never sent a message left its entry in memory forever.
 */
package util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.Duration;

public class SessionKeyCache {

    /** How long a negotiated session key stays usable if the client never calls /eservice. */
    static final Duration DEFAULT_TTL = Duration.ofMinutes(5);

    /**
     * Upper bound on pending sessions. /keyexchange is unauthenticated, so without a
     * cap a client could fill the heap within one TTL. Oldest entries are evicted first.
     */
    static final long MAX_SESSIONS = 100_000;

    private final Cache<String, DHUtil> cache;

    /** Creates a cache whose entries expire {@link #DEFAULT_TTL} after they are written. */
    public SessionKeyCache() {
        this(DEFAULT_TTL);
    }

    /** Creates a cache whose entries expire {@code ttl} after they are written. */
    public SessionKeyCache(Duration ttl) {
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(ttl).maximumSize(MAX_SESSIONS).build();
    }

    /** Stores the session key for a newly negotiated session. */
    public void put(String sessionId, DHUtil util) {
        cache.put(sessionId, util);
    }

    /** Returns the session key, or null if the session is unknown or has expired. */
    public DHUtil get(String sessionId) {
        return cache.getIfPresent(sessionId);
    }

    /**
     * Removes the session and returns it, or returns null. Called after the response
     * is encrypted, so each session key protects exactly one request/response pair.
     */
    public DHUtil use(String sessionId) {
        return cache.asMap().remove(sessionId);
    }

    @Override
    public String toString() {
        return "SessionKeyCache{size=" + cache.size() + '}';
    }
}
