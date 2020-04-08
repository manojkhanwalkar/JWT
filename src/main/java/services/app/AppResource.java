package services.app;


import com.codahale.metrics.annotation.Timed;
import data.*;
import services.authenticator.LoginManager;
import util.Connection;
import util.JSONUtil;
import util.JWSVerifyUtil;
import util.KeyExchangeManager;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



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




    @POST
    @Timed
    @Path("/keyexchange")
    @Produces(MediaType.APPLICATION_JSON)
    public KeyExchangeResponse exchange(KeyExchangeRequest request) {


        KeyExchangeManager keyExchangeManager = new KeyExchangeManager();
        return keyExchangeManager.processExchange(request);


    }



 /*   @POST
    @Timed
    @Path("/eservice")
    @Produces(MediaType.APPLICATION_JSON)
    public EncryptedMessage verify(EncryptedMessage request) {

       keyExchangeManager.verify(request);


        String requestStr = keyExchangeManager.decryptRequest(request);


        // Encrypted Verified claim to be processed here .
        EncryptedVerifiedClaim encryptedVerifiedClaim = (EncryptedVerifiedClaim) JSONUtil.fromJSON(requestStr,EncryptedVerifiedClaim.class);

        VerifiedClaim claim = convert(encryptedVerifiedClaim,rsaKeyHolder.getPrivateKey());

        isValidClaim(claim);

        System.out.println(claim);

        CombinedCreditScore combinedCreditScore = new CombinedCreditScore();


        CreditManagerFactory creditManagerFactory = CreditManagerFactory.getInstance();

        creditManagerFactory.get().parallelStream().forEach(creditManager -> {

            CreditScore creditScore = creditManager.getCreditScore();
            creditScore.setBureau(creditManager.bureau);
            combinedCreditScore.addScore(creditScore);

        });



        String responseStr = JSONUtil.toJSON(combinedCreditScore);
        EncryptedMessage response =  keyExchangeManager.encryptResponse(responseStr,request);
        keyExchangeManager.sign(response);
        return response;


    }

*/












}
