/*
 * JWSSignUtil
 *
 * Issues JSON Web Tokens signed with ES256 (ECDSA on P-256 with SHA-256). It is
 * used by the authorizer service to mint tokens. The public half is published on
 * GET /certificate so that the app service can check the tokens.
 *
 * Every token carries registered claims so a verifier can bound its use:
 *   iss  ISSUER        who minted it
 *   aud  AUDIENCE      which service may accept it
 *   sub  the user id   whose session it was issued for
 *   iat / exp          issued-at and expiry (TOKEN_TTL)
 *   jti                unique id
 *
 * Key file locations can be set with system properties:
 *   -Djws.certFile=...   X.509 certificate that holds the EC public key
 *   -Djws.keyFile=...    PKCS#8 DER EC private key
 */
package util;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;

public class JWSSignUtil {

    static final String DEFAULT_CERT_FILE = "/home/manoj/IdeaProjects/KryptIdEcoSysDemo/src/main/resources/clientcert.pem";
    static final String DEFAULT_KEY_FILE = "/home/manoj/IdeaProjects/KryptIdEcoSysDemo/src/main/resources/clientkey.der";

    /** "iss" claim of every token this service issues. */
    public static final String ISSUER = "jwt-demo-authorizer";

    /** "aud" claim: the app (resource) service is the only intended recipient. */
    public static final String AUDIENCE = "jwt-demo-app";

    /** How long an issued token stays valid. */
    public static final Duration TOKEN_TTL = Duration.ofMinutes(5);

    private final PrivateKey privateKey;

    private final String publicKeyStr;

    /** Loads the signing key pair from the configured (or default) files. */
    public JWSSignUtil() {
        this(System.getProperty("jws.certFile", DEFAULT_CERT_FILE), System.getProperty("jws.keyFile", DEFAULT_KEY_FILE));
    }

    /** Loads the signing key pair from the given certificate and DER private-key files. */
    public JWSSignUtil(String certFile, String privateKeyFile) {
        this(CryptUtil.getPrivateKeyDerFromFile(privateKeyFile, "EC"), CryptUtil.getPublicKeyFromCertFile(certFile, "EC"));
    }

    /** Uses an in-memory key pair (for tests). */
    public JWSSignUtil(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKeyStr = CryptUtil.convertPublicKeyToString(publicKey);
    }

    /** Returns the Base64 X.509 encoding of the verification key, for publishing to verifiers. */
    public String getPublicKey() {
        return publicKeyStr;
    }

    /**
     * Issues a signed JWT for {@code subject} with iss, aud, iat, exp and jti claims.
     *
     * @return the compact serialization (header.payload.signature)
     * @throws IllegalStateException if signing fails
     */
    public String issueToken(String subject) {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(ISSUER);
        claims.setAudience(AUDIENCE);
        claims.setSubject(subject);
        claims.setIssuedAtToNow();
        claims.setExpirationTimeMinutesInTheFuture(TOKEN_TTL.toSeconds() / 60f);
        claims.setGeneratedJwtId();
        return sign(claims.toJson());
    }

    /**
     * Signs {@code messageToSign} as a raw JWS payload. Such a token has no claims,
     * so {@link JWSVerifyUtil#verifyJWS} rejects it; use {@link #issueToken} for access tokens.
     *
     * @return the compact serialization (header.payload.signature)
     * @throws IllegalStateException if signing fails
     */
    public String jwsSign(String messageToSign) {
        return sign(messageToSign);
    }

    private String sign(String payload) {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(payload);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);
        jws.setKey(privateKey);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new IllegalStateException("JWS signing failed", e);
        }
    }
}
