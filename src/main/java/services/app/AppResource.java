package services.app;


import com.codahale.metrics.annotation.Timed;
import data.*;
import services.authenticator.LoginManager;
import util.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class AppResource {
    private final String template;
    private final String defaultName;



JWSVerifyUtil jwsVerifyUtil; //  = new JWSVerifyUtil();



    public AppResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;


        initializeJWS();

    }


    private void initializeJWS()
    {

        try {
            Connection auth = new Connection("https://localhost:8280/");

            String certJson = auth.get("certificate");

            String cert = (String)JSONUtil.fromJSON(certJson,String.class);

            jwsVerifyUtil = new JWSVerifyUtil(cert);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @POST
    @Timed
    @Path("/service")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse claims(AppRequest request) {

        AppResponse response=null;

        if (jwsVerifyUtil.verifyJWS(request.getToken())) {
            // validate jwt and if valid , process the service request
            //   return loginManager.logout(request);
            response = new AppResponse(AppResponse.Status.Valid, request.getDummy().toUpperCase());
        }
        else
        {
            response = new AppResponse(AppResponse.Status.Invalid,"");
        }

        return response;


    }


    private static final String dir = "/home/manoj/IdeaProjects/JWT/src/main/resources/files/";

    @POST
    @Timed
    @Path("/meta")
    @Produces(MediaType.APPLICATION_JSON)
    public FileMetaResponse exchange(FileMetaRequest request) {

        // TODO - validate the JWT

        String checkSum = CheckSum.checkSum(dir+request.getFileName() );

        File file = new File(dir+request.getFileName());



        FileMetaResponse response = new FileMetaResponse((int)file.length(),checkSum);

        return response;


    }


    @POST
    @Timed
    @Path("/chunk")
    @Produces(MediaType.APPLICATION_JSON)
    public ChunkResponse exchange(ChunkRequest request) {

        // TODO - validate the JWT
        String fileName = dir+request.getFileName();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.skip(request.getStart()); // chars to skip

            int size = request.getEnd()-request.getStart();

            char[] buf = new char[size];

            int charsRead = reader.read(buf);

       //     System.out.println(charsRead);



            ChunkResponse response = new ChunkResponse();
            response.setContents(String.valueOf(buf));

//            System.out.println(response.getContents().length());

            return response ;

        } catch (IOException io)
        {
            io.printStackTrace();
        }

        return null;


    }




    @POST
    @Timed
    @Path("/keyexchange")
    @Produces(MediaType.APPLICATION_JSON)
    public KeyExchangeResponse exchange(KeyExchangeRequest request) {


        KeyExchangeManager keyExchangeManager = new KeyExchangeManager();
        return keyExchangeManager.processExchange(request);


    }



   @POST
    @Timed
    @Path("/eservice")
    @Produces(MediaType.APPLICATION_JSON)
    public EncryptedMessage verify(EncryptedMessage request) {




        String requestStr = KeyExchangeManager.decryptRequest(request);



        String responseStr = JSONUtil.toJSON(requestStr.toUpperCase());
        EncryptedMessage response =  KeyExchangeManager.encryptResponse(responseStr,request);

        return response;


    }














}
