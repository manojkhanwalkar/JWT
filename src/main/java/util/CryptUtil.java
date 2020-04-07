package util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.util.Base64.getEncoder;

public class CryptUtil {



    private static byte[] extractPublicKeyFromFile(String fileName)
    {
        try {
            File privKeyFile = new File(fileName);
            // read private key DER file
            DataInputStream dis = new DataInputStream(new FileInputStream(privKeyFile));
            byte[] privKeyBytes = new byte[(int)privKeyFile.length()];
            dis.read(privKeyBytes);
            dis.close();

            return privKeyBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static PublicKey getPublicKey(byte[] bytes)
    {
        try {

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            X509Certificate cer = (X509Certificate) fact.generateCertificate(bis);
            PublicKey key = cer.getPublicKey();


       //     System.out.println(cer);
            return key;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String convertPublicKeyToString(PublicKey key)
    {


        // public key to bytes , bytes to public key
        String publicKeyStr= getEncoder().encodeToString(key.getEncoded());
        return publicKeyStr;

    }


    public static String convertPrivateKeyToString(PrivateKey key)
    {



        String privateKeyStr= getEncoder().encodeToString(key.getEncoded());
        return privateKeyStr;

    }


    public static PublicKey convertStringtoPublicKey(String str, String type)
    {
        try{
            byte[] byteKey = Base64.getDecoder().decode(str.getBytes());
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance(type);

            return kf.generatePublic(X509publicKey);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public static PrivateKey convertStringtoPrivateKey(String str, String type)
    {
        try{
            byte[] byteKey = Base64.getDecoder().decode(str.getBytes());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(spec);


           /*
            X509EncodedKeySpec X509privateKey= new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance(type);

            return kf.generatePrivate(X509privateKey); */

           return privKey;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    static SecureRandom secureRandom = new SecureRandom();






    public static PublicKey getPublicKeyFromCertFile(String file,String keyType)
    {
        return getPublicKey(extractPublicKeyFromFile(file));
    }



    public static PrivateKey getPrivateKeyDerFromFile(String file, String keyType){
        try {
            File privKeyFile = new File(file);
            // read private key DER file
            DataInputStream dis = new DataInputStream(new FileInputStream(privKeyFile));
            byte[] privKeyBytes = new byte[(int)privKeyFile.length()];
            dis.read(privKeyBytes);
            dis.close();

            KeyFactory kf = KeyFactory.getInstance(keyType);
            // decode private key
            PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKeyBytes);
            PrivateKey privKey = kf.generatePrivate(privSpec);

            return privKey;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }



    public static void main(String[] args) throws Exception {

  /*
     private static final String ALGO = "SHA256withECDSA";

  PublicKey pub =  getPublicKey(extractPublicKeyFromFile("/home/manoj/IdeaProjects/phoenix/src/main/resources/idpcertificate.pem"));
        PrivateKey priv = loadPrivateKey("/home/manoj/IdeaProjects/phoenix/src/main/resources/idpprivatekey.der");

        String plainText = "Hello World from EC Public and Private keys";

        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(priv);
        ecdsaSign.update(plainText.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();


        Signature ecdsaVerify = Signature.getInstance(ALGO);

        ecdsaVerify.initVerify(pub);
        ecdsaVerify.update(plainText.getBytes("UTF-8"));
        boolean result = ecdsaVerify.verify(signature);


        System.out.println(result);*/



    }



}
