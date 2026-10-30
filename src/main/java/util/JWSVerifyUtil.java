/*
 * JWSVerifyUtil
 *
 * Checks ES256 JWTs made by JWSSignUtil#issueToken. A token is accepted only if:
 *   - it is signed with ES256 (the whitelist blocks algorithm substitution, e.g. "alg":"none"),
 *   - the signature checks out against the authorizer's public key,
 *   - iss and aud match JWSSignUtil.ISSUER / AUDIENCE,
 *   - it has a subject and has not expired (exp is required).
 *
 * The verification key comes either from the default certificate file or from the
 * Base64 public key that the authorizer publishes at GET /certificate.
 */
package util;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.security.PublicKey;

public class JWSVerifyUtil {

    private static final AlgorithmConstraints ES256_ONLY = new AlgorithmConstraints(
            AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);

    /** Tolerated clock difference between the authorizer and this service. */
    private static final int CLOCK_SKEW_SECONDS = 30;

    private final JwtConsumer consumer;

    /** Loads the verification key from the configured (or default) certificate file. */
    public JWSVerifyUtil() {
        this(CryptUtil.getPublicKeyFromCertFile(
                System.getProperty("jws.certFile", JWSSignUtil.DEFAULT_CERT_FILE), "EC"));
    }

    /** Uses a Base64 X.509 EC public key, as returned by the authorizer's /certificate endpoint. */
    public JWSVerifyUtil(String cert) {
        this(CryptUtil.convertStringtoPublicKey(cert, "EC"));
    }

    /** Uses the given EC public key. */
    public JWSVerifyUtil(PublicKey publicKey) {
        consumer = new JwtConsumerBuilder()
                .setJwsAlgorithmConstraints(ES256_ONLY)
                .setVerificationKey(publicKey)
                .setRequireExpirationTime()
                .setRequireSubject()
                .setAllowedClockSkewInSeconds(CLOCK_SKEW_SECONDS)
                .setExpectedIssuer(JWSSignUtil.ISSUER)
                .setExpectedAudience(JWSSignUtil.AUDIENCE)
                .build();
    }

    /**
     * Returns true only if {@code token} is a well-formed, unexpired ES256 JWT from
     * our issuer, for our audience, whose signature checks out against our public key.
     * A malformed or invalid token returns false and does not throw.
     */
    public boolean verifyJWS(String token) {
        if (token == null) {
            return false;
        }
        try {
            consumer.processToClaims(token);
            return true;
        } catch (InvalidJwtException e) {
            return false;
        }
    }
}
