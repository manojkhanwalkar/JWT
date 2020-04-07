package services.app;


import com.codahale.metrics.annotation.Timed;
import data.*;
import services.authenticator.LoginManager;
import util.JWSVerifyUtil;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class AppResource {
    private final String template;
    private final String defaultName;



JWSVerifyUtil jwsVerifyUtil = new JWSVerifyUtil();



    public AppResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;




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


   /* @POST
    @Timed
    @Path("/verify")
    @Produces(MediaType.APPLICATION_JSON)
    public SessionVerifyResponse claims(SessionVerifyRequest request) {


        return loginManager.verify(request);



    }*/






 /*   @POST
    @Timed
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public LoginResponse claims(LoginRequest request) {


        return loginManager.login(request);



    }*/








}
