/*
 * JWSSignUtil
 *
 * Issues JSON Web Signatures (compact JWS / JWT form) signed with ES256 (ECDSA on
 * P-256 with SHA-256). It is used by the authorizer service to mint tokens. The
 * public half is published on GET /certificate so that the app service can check
 * the tokens.
 *
 * Key file locations can be set with system properties:
 *   -Djws.certFile=...   X.509 certificate that holds the EC public key
 *   -Djws.keyFile=...    PKCS#8 DER EC private key
 */
package util;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JWSSignUtil {

    static final String DEFAULT_CERT_FILE = "/home/manoj/IdeaProjects/KryptIdEcoSysDemo/src/main/resources/clientcert.pem";
    static final String DEFAULT_KEY_FILE = "/home/manoj/IdeaProjects/KryptIdEcoSysDemo/src/main/resources/clientkey.der";

    private final PrivateKey privateKey;

    private final String publicKeyStr;

    /** Loads the signing key pair from the configured (or default) files. */
    public JWSSignUtil() {
        this(System.getProperty("jws.certFile", DEFAULT_CERT_FILE), System.getProperty("jws.keyFile", DEFAULT_KEY_FILE));
    }

    /** Loads the signing key pair from the given certificate and DER private-key files. */
    public JWSSignUtil(String certFile, String privateKeyFile) {
        this.privateKey = CryptUtil.getPrivateKeyDerFromFile(privateKeyFile, "EC");
        PublicKey publicKey = CryptUtil.getPublicKeyFromCertFile(certFile, "EC");
        this.publicKeyStr = CryptUtil.convertPublicKeyToString(publicKey);
    }

    /** Returns the Base64 X.509 encoding of the verification key, for publishing to verifiers. */
    public String getPublicKey() {
        return publicKeyStr;
    }

    /**
     * Signs {@code messageToSign} as the JWS payload.
     *
     * @return the compact serialization (header.payload.signature), or null if signing failed
     */
    public String jwsSign(String messageToSign) {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(messageToSign);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);
        jws.setKey(privateKey);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
