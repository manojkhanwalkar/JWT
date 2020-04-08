package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import data.EncryptedMessage;
import data.KeyExchangeRequest;
import data.KeyExchangeResponse;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

public class Connection {

    String target;

    HttpClient client ;


    private static final String SPEC = "secp256k1";


    private void initConnectionProps()
    {
        final Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());

    }

    public Connection(String target)
    {
        this.target = target;
        initConnectionProps();
        client = HttpClient.newHttpClient();
    }



    public String sendSimple(String request,String action) throws Exception
    {
        return post(request,action);
    }




    private String post(String str , String url) throws Exception
    {

        HttpRequest httprequest = HttpRequest.newBuilder()
                .uri(URI.create(target + url))
                .header("Content-Type", "application/json")
               //  .header("HMAC" , HMACUtil.calculateHMAC(str,HmacKey))
                .POST(HttpRequest.BodyPublishers.ofString(str))
                .timeout(Duration.ofMinutes(1))
                .build();


        HttpResponse<String> response =
                client.send(httprequest, HttpResponse.BodyHandlers.ofString());

        return response.body();


    }



    public String get( String url) throws Exception
    {

        HttpRequest httprequest = HttpRequest.newBuilder()
                .uri(URI.create(target + url))
                .GET()
                .timeout(Duration.ofMinutes(1))
                .build();


        HttpResponse<String> response =
                client.send(httprequest, HttpResponse.BodyHandlers.ofString());

        return response.body();


    }

    static SecureRandom secureRandom = new SecureRandom();


    public String esend(String message, String eservice) throws Exception{



            ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
            KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
            g.initialize(ecSpec, new SecureRandom());
            KeyPair keypair = g.generateKeyPair();

            PublicKey publicKey = keypair.getPublic();
            PrivateKey privateKey = keypair.getPrivate();


            KeyExchangeRequest request = new KeyExchangeRequest(CryptUtil.convertPublicKeyToString(publicKey));

            String str = post(JSONUtil.toJSON(request),"keyexchange");

            KeyExchangeResponse response = (KeyExchangeResponse) JSONUtil.fromJSON(str,KeyExchangeResponse.class);

              PublicKey pubKey = CryptUtil.convertStringtoPublicKey(response.getPublicKey(),"EC");

              DHUtil util = new DHUtil(publicKey,privateKey);

                util.setReceiverPublicKey(pubKey);

                // key exchange completed , now send the encrypted message and get encrypted response .


        byte[] iv = new byte[128/8];
        secureRandom.nextBytes(iv);

        String encMessage = util.encrypt(message,iv);


        EncryptedMessage encryptedMessage = new EncryptedMessage();
        encryptedMessage.setMessage(encMessage);
        encryptedMessage.setSessionId(response.getSessionId());
        encryptedMessage.setIv(Base64.getEncoder().encodeToString(iv));



        String encryptedResponseStr = post(JSONUtil.toJSON(encryptedMessage),eservice);

        encryptedMessage = (EncryptedMessage)JSONUtil.fromJSON(encryptedResponseStr,EncryptedMessage.class);

       ;

        String decMessage = util.decrypt(encryptedMessage.getMessage(),encryptedMessage.getIv());

        return decMessage;

    }
}
