package util;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;




public class JWSSignUtil {

    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";

    String certFile = "/home/manoj/IdeaProjects/KryptIdEcoSysDemo/src/main/resources/clientcert.pem";

    private final PrivateKey privateKey;

    private final PublicKey publicKey;

    private final String publicKeyStr ;

    String privateFile = "/home/manoj/IdeaProjects/KryptIdEcoSysDemo/src/main/resources/clientkey.der";





    public JWSSignUtil()
    {

        this.privateKey =   CryptUtil.getPrivateKeyDerFromFile(privateFile,"EC");
        this.publicKey = CryptUtil.getPublicKeyFromCertFile(certFile,"EC");

        publicKeyStr = CryptUtil.convertPublicKeyToString(publicKey);

    }


    public String getPublicKey()
    {
        return publicKeyStr;
    }



    public String jwsSign(String messageToSign) {

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(messageToSign);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);
        jws.setKey(privateKey);
        String jwsCompactSerialization = null;
        try {
            jwsCompactSerialization = jws.getCompactSerialization();

            System.out.println(jwsCompactSerialization);
        } catch (JoseException e) {
            e.printStackTrace();
        }


        return jwsCompactSerialization;

    }









}



