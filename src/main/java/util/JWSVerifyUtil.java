package util;


import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;


// using the same keys for signing and key exchange - in practice a separate set of keys will be used.


public class JWSVerifyUtil {

    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";
  //  private final String certificateFile;
  //  private final String privateKeyFile;



    String certFile = "/home/manoj/IdeaProjects/KryptIdEcoSysDemo/src/main/resources/clientcert.pem";

    PublicKey publicKey ;


    public JWSVerifyUtil()
    {

        publicKey = CryptUtil.getPublicKeyFromCertFile(certFile,"EC");

    }


    public JWSVerifyUtil(String cert)
    {

        publicKey = CryptUtil.convertStringtoPublicKey(cert,"EC");

    }








    public boolean verifyJWS(String token) {




        JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256));
        boolean signatureVerified = false;
        try {


            jws.setCompactSerialization(token);
            jws.setKey(publicKey);
            signatureVerified = jws.verifySignature();
        } catch (JoseException e) {
            e.printStackTrace();
        }
        System.out.println("JWS Signature is valid: " + signatureVerified);

        return signatureVerified;

      /*  String payload = jws.getPayload();
        System.out.println("JWS payload: " + payload); */




    }
}



