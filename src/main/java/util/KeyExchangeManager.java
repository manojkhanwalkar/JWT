package util;


import data.EncryptedMessage;
import data.KeyExchangeRequest;
import data.KeyExchangeResponse;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.UUID;

import static util.CryptUtil.secureRandom;

public class KeyExchangeManager {



    static SessionKeyCache sessionKeyCache = new SessionKeyCache();

    PublicKey publicKey;
    PrivateKey privateKey;


  /*  public KeyExchangeManager(PublicKey publicKey, PrivateKey privateKey)
    {
        this.privateKey = privateKey;
        this.publicKey = publicKey;

    }*/

    private static final String SPEC = "secp256k1";

    public KeyExchangeManager()
    {
        // create a public and private key pair everytime

        try {
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
            KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
            g.initialize(ecSpec, new SecureRandom());
            KeyPair keypair = g.generateKeyPair();
            publicKey = keypair.getPublic();
            privateKey = keypair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }


    }


    DHUtil util;


  //  SignUtil digiSig ;

  //  SignUtil verifySig ;
//


    public KeyExchangeResponse processExchange(KeyExchangeRequest request) {
       String responseKey = request.getPublicKey();

        PublicKey pubKey = CryptUtil.convertStringtoPublicKey(responseKey,"EC");

      /*  String responseCertStr = request.getCertStr();
        PublicKey pubKey = CertUtil.getCertFromString(responseCertStr).getPublicKey();*/

        util = new DHUtil(publicKey,privateKey);

        util.setReceiverPublicKey(pubKey);


        KeyExchangeResponse response = new KeyExchangeResponse();
       String encKey = CryptUtil.convertPublicKeyToString(publicKey);

       // String certStr = CertUtil.getCertAsString(certificate);
        response.setPublicKey(encKey);

        String sessionId = UUID.randomUUID().toString();

       response.setSessionId(sessionId);

        sessionKeyCache.put(sessionId,util);

      //  verifySig = new SignUtil(publicKey,privateKey,certificate);
        return response;

    }


    public static String decryptRequest(EncryptedMessage request)
    {
        DHUtil util = sessionKeyCache.get(request.getSessionId());
        return decrypt(request,util);
    }



    public static EncryptedMessage encryptResponse(String response, EncryptedMessage request)
    {
        DHUtil util = sessionKeyCache.use(request.getSessionId());

        return encrypt(response,util);

    }




    private static String decrypt(EncryptedMessage request, DHUtil util)
    {
        return util.decrypt(request.getMessage(),request.getIv());



    }




    private static EncryptedMessage encrypt(String str, DHUtil util)
    {
        byte[] iv = new byte[128/8];
        secureRandom.nextBytes(iv);

        String estr = util.encrypt(str,iv);
        EncryptedMessage message = new EncryptedMessage();
        message.setMessage(estr);
        message.setIv(Base64.getEncoder().encodeToString(iv));
        return message;
    }






}
