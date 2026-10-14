/*
 * JWSVerifyUtil
 *
 * Checks ES256 JSON Web Signatures made by JWSSignUtil. Only ES256 is accepted. The
 * whitelist blocks algorithm-substitution attacks, for example "alg":"none".
 *
 * The verification key comes either from the default certificate file or from the
 * Base64 public key that the authorizer publishes at GET /certificate.
 */
package util;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

import java.security.PublicKey;

public class JWSVerifyUtil {

    private static final AlgorithmConstraints ES256_ONLY = new AlgorithmConstraints(
            AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);

    private final PublicKey publicKey;

    /** Loads the verification key from the configured (or default) certificate file. */
    public JWSVerifyUtil() {
        publicKey = CryptUtil.getPublicKeyFromCertFile(
                System.getProperty("jws.certFile", JWSSignUtil.DEFAULT_CERT_FILE), "EC");
    }

    /** Uses a Base64 X.509 EC public key, as returned by the authorizer's /certificate endpoint. */
    public JWSVerifyUtil(String cert) {
        publicKey = CryptUtil.convertStringtoPublicKey(cert, "EC");
    }

    /**
     * Returns true only if {@code token} is a well-formed ES256 JWS whose signature
     * checks out against our public key. A malformed token returns false and does
     * not throw.
     */
    public boolean verifyJWS(String token) {
        if (token == null) {
            return false;
        }
        JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmConstraints(ES256_ONLY);
        try {
            jws.setCompactSerialization(token);
            jws.setKey(publicKey);
            return jws.verifySignature();
        } catch (JoseException e) {
            return false;
        }
    }
}
