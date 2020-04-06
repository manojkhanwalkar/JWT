package client;

import data.LoginRequest;
import data.LoginResponse;
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

        System.out.println(str1);


    }

}
