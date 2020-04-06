package services.authorizer;


import com.codahale.metrics.annotation.Timed;
import data.AppRequest;
import data.AppResponse;
import data.TokenRequest;
import data.TokenResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    private final String template;
    private final String defaultName;







    public AuthResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;




    }



    @POST
    @Timed
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public TokenResponse claims(TokenRequest request) {


        // validate sessionid and if valid generate jwt
     //   return loginManager.logout(request);
        TokenResponse response = new TokenResponse();

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
