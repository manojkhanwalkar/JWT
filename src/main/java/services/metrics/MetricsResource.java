package services.metrics;


import com.codahale.metrics.annotation.Timed;
import data.SessionVerifyRequest;
import data.SessionVerifyResponse;
import data.TokenRequest;
import data.TokenResponse;
import util.Connection;
import util.JSONUtil;
import util.JWSSignUtil;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class MetricsResource {
    private final String template;
    private final String defaultName;





    JWSSignUtil jwsSignUtil = new JWSSignUtil();

    public MetricsResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;




    }



    @POST
    @Timed
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public TokenResponse claims(TokenRequest request) {

        TokenResponse response=null;

        Connection login = new Connection("https://localhost:8480/");

        SessionVerifyRequest verifyRequest = new SessionVerifyRequest(request.getSessionId());

        try {
            String str1 = login.sendSimple(JSONUtil.toJSON(verifyRequest),"verify");

            SessionVerifyResponse sessionVerifyResponse = (SessionVerifyResponse)JSONUtil.fromJSON(str1,SessionVerifyResponse.class);


            if (sessionVerifyResponse.getStatus()== SessionVerifyResponse.Status.Valid)
            {
                response  = new TokenResponse(TokenResponse.Status.Valid, jwsSignUtil.jwsSign("Dummy Token"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response==null)
        {
            response =  new TokenResponse(TokenResponse.Status.Invalid, UUID.randomUUID().toString());
        }




        return response;


    }


    @GET
    @Timed
    @Path("/certificate")
    @Produces(MediaType.APPLICATION_JSON)
    public String claims() {


        return JSONUtil.toJSON(jwsSignUtil.getPublicKey());



    }






 /*   @POST
    @Timed
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public LoginResponse claims(LoginRequest request) {


        return loginManager.login(request);



    }*/








}
