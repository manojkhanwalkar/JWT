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

public final class PasswordHasher {

    private static final String PREFIX = "pbkdf2-sha256";
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    static final int ITERATIONS = 600_000;
    private static final int SALT_BYTES = 16;
    private static final int HASH_BITS = 256;

    private static final SecureRandom RANDOM = new SecureRandom();

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
            return MessageDigest.isEqual(expected, pbkdf2(password.toCharArray(), salt, iterations));
        } catch (IllegalArgumentException e) {
            return false;
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
