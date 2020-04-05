package services.authenticator;


import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;

import data.*;


import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;



@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {
    private final String template;
    private final String defaultName;


    LoginManager loginManager;




    public LoginResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;

        loginManager = new LoginManager();


    }



    @POST
    @Timed
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public LogoutResponse claims(LogoutRequest request) {


        return loginManager.logout(request);



    }





    @POST
    @Timed
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public LoginResponse claims(LoginRequest request) {


        return loginManager.login(request);



    }








}
