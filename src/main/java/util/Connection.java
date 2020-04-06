package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

public class Connection {

    String target;

    HttpClient client ;





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





}
