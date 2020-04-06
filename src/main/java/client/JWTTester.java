package client;

import com.amazonaws.services.opsworks.model.App;
import data.*;
import util.Connection;
import util.JSONUtil;

public class JWTTester {

    public static void main(String[] args) throws Exception{

        Connection login = new Connection("https://localhost:8480/");
        Connection auth = new Connection("https://localhost:8280/");
        Connection  app = new Connection("https://localhost:8380/");


        LoginRequest request = new LoginRequest("user1","password1");

        String str = JSONUtil.toJSON(request);

       // LoginResponse response = loginManager.login(request)

        String str1 = login.sendSimple(str,"login");

        LoginResponse response = (LoginResponse)JSONUtil.fromJSON(str1,LoginResponse.class);

        System.out.println(str1);

        TokenRequest tokenRequest = new TokenRequest(response.getSessionId());



        TokenResponse tokenResponse = (TokenResponse)JSONUtil.fromJSON(auth.sendSimple(JSONUtil.toJSON(tokenRequest),"token"),TokenResponse.class);

        System.out.println(tokenResponse);

        AppRequest appRequest = new AppRequest(tokenResponse.getJwt(),"Dummy service request");

        String str2 = app.sendSimple(JSONUtil.toJSON(appRequest),"service");

        AppResponse appResponse = (AppResponse) JSONUtil.fromJSON(str2,AppResponse.class);

        System.out.println(appResponse);

    }

}
