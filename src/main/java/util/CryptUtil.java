/*
 * CryptUtil
 *
 * Static helpers for keys:
 *   - generate ephemeral EC key pairs for ECDH key exchange
 *   - convert public and private keys to and from Base64 strings so they can travel in JSON
 *   - load a public key from an X.509 certificate file and a private key from a PKCS#8 DER file
 */
package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class CryptUtil {

    /**
     * Curve used for ephemeral ECDH keys. This is NIST P-256, the same curve as the
     * ES256 JWS signatures. secp256k1 was used before, but the JDK removed it in
     * Java 16, so key generation failed on the Java 21+ runtime this project targets.
     */
    public static final String EC_CURVE = "secp256r1";

    /** Shared CSPRNG. SecureRandom is thread-safe, and seeding is expensive, so it is reused. */
    public static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private CryptUtil() {
    }

    /** Generates a fresh EC key pair on {@link #EC_CURVE} for one key exchange. */
    public static KeyPair generateECKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
            generator.initialize(new ECGenParameterSpec(EC_CURVE), SECURE_RANDOM);
            return generator.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("EC key generation unavailable for " + EC_CURVE, e);
        }
    }

    /** Base64-encodes a public key's X.509 (SubjectPublicKeyInfo) form. */
    public static String convertPublicKeyToString(PublicKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /** Base64-encodes a private key's PKCS#8 form. */
    public static String convertPrivateKeyToString(PrivateKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * Decodes a Base64 X.509 public key made by {@link #convertPublicKeyToString}.
     *
     * @param type key algorithm, for example "EC" or "RSA"
     * @throws IllegalArgumentException if the string is not a valid key of that type
     */
    public static PublicKey convertStringtoPublicKey(String str, String type) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(str);
            return KeyFactory.getInstance(type).generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Invalid " + type + " public key", e);
        }
    }

    /**
     * Decodes a Base64 PKCS#8 private key made by {@link #convertPrivateKeyToString}.
     *
     * @param type key algorithm, for example "EC" or "RSA"
     * @throws IllegalArgumentException if the string is not a valid key of that type
     */
    public static PrivateKey convertStringtoPrivateKey(String str, String type) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(str);
            return KeyFactory.getInstance(type).generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Invalid " + type + " private key", e);
        }
    }

    /**
     * Reads an X.509 certificate (PEM or DER) and returns its public key.
     *
     * @param keyType kept for API compatibility. The algorithm comes from the certificate.
     */
    public static PublicKey getPublicKeyFromCertFile(String file, String keyType) {
        try (InputStream in = Files.newInputStream(Path.of(file))) {
            return CertificateFactory.getInstance("X.509").generateCertificate(in).getPublicKey();
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read certificate " + file, e);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Invalid certificate " + file, e);
        }
    }

    /** Reads a PKCS#8 DER-encoded private key of the given algorithm from a file. */
    public static PrivateKey getPrivateKeyDerFromFile(String file, String keyType) {
        try {
            byte[] keyBytes = Files.readAllBytes(Path.of(file));
            return KeyFactory.getInstance(keyType).generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read private key " + file, e);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Invalid " + keyType + " private key " + file, e);
        }
    }
}
