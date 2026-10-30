/*
 * PasswordHasher
 *
 * Salted, slow password hashing with PBKDF2-HMAC-SHA256. A stored hash has the form
 *   pbkdf2-sha256$<iterations>$<Base64 salt>$<Base64 hash>
 * so the iteration count can be raised later without breaking existing records.
 */
package dynamodb;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public final class PasswordHasher {

    private static final String PREFIX = "pbkdf2-sha256";
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    static final int ITERATIONS = 600_000;
    private static final int SALT_BYTES = 16;
    private static final int HASH_BITS = 256;

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Caps concurrent hashing at one per core, so a flood of /login requests (which
     * no per-user lockout can stop) cannot take every CPU. A check that cannot get
     * a slot within HASH_WAIT fails closed.
     */
    private static final Semaphore HASH_SLOTS = new Semaphore(Runtime.getRuntime().availableProcessors(), true);
    private static final long HASH_WAIT_MS = 5_000;

    /** Hash of a random password, checked when a user does not exist so the timing matches. */
    private static final String DUMMY_HASH = hash(Base64.getEncoder().encodeToString(RANDOM.generateSeed(16)));

    private PasswordHasher() {
    }

    /** Returns a new salted hash of {@code password} in the stored format. */
    public static String hash(String password) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] hash = pbkdf2(password.toCharArray(), salt, ITERATIONS);
        Base64.Encoder b64 = Base64.getEncoder();
        return PREFIX + "$" + ITERATIONS + "$" + b64.encodeToString(salt) + "$" + b64.encodeToString(hash);
    }

    /**
     * Returns true if {@code password} matches {@code stored}. A null or malformed
     * stored value (for example a legacy plaintext password) never matches, but the
     * same amount of hashing work is still done.
     */
    public static boolean verify(String password, String stored) {
        if (password == null) {
            return false;
        }
        String[] parts = stored == null ? null : stored.split("\\$");
        if (parts == null || parts.length != 4 || !PREFIX.equals(parts[0])) {
            verify(password, DUMMY_HASH);
            return false;
        }
        try {
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            if (iterations < 1 || iterations > 10_000_000) {
                return false;
            }
            return MessageDigest.isEqual(expected, limitedPbkdf2(password.toCharArray(), salt, iterations));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /** pbkdf2 under HASH_SLOTS. Returns an empty array (never a match) if no slot frees up in time. */
    private static byte[] limitedPbkdf2(char[] password, byte[] salt, int iterations) {
        try {
            if (!HASH_SLOTS.tryAcquire(HASH_WAIT_MS, TimeUnit.MILLISECONDS)) {
                return new byte[0];
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new byte[0];
        }
        try {
            return pbkdf2(password, salt, iterations);
        } finally {
            HASH_SLOTS.release();
        }
    }

    /** Burns the same work as a real check. Used when the user does not exist. */
    public static void dummyVerify(String password) {
        verify(password == null ? "" : password, DUMMY_HASH);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, HASH_BITS);
        try {
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 unavailable", e);
        } finally {
            spec.clearPassword();
        }
    }
}
