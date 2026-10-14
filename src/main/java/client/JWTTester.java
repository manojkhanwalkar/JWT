/*
 * JWTTester
 *
 * Manual end-to-end test of the whole JWT flow across the three services:
 *   1. log in to the authenticator (:8480) and get a session id
 *   2. trade the session id with the authorizer (:8280) for a signed JWT
 *   3. call the app service (:8380) with the JWT. The app checks the signature and
 *      echoes the payload in upper case.
 */
package client;

import data.AppRequest;
import data.AppResponse;
import data.LoginRequest;
import data.LoginResponse;
import data.TokenRequest;
import data.TokenResponse;
import util.Connection;
import util.JSONUtil;

public class JWTTester {

    /** Runs login, then token, then service, and prints each response. */
    public static void main(String[] args) throws Exception {
        Connection login = new Connection("https://localhost:8480/");
        Connection auth = new Connection("https://localhost:8280/");
        Connection app = new Connection("https://localhost:8380/");

        String loginJson = login.sendSimple(JSONUtil.toJSON(new LoginRequest("user1", "password1")), "login");
        LoginResponse response = JSONUtil.fromJSON(loginJson, LoginResponse.class);
        System.out.println(loginJson);

        String tokenJson = auth.sendSimple(JSONUtil.toJSON(new TokenRequest(response.getSessionId())), "token");
        TokenResponse tokenResponse = JSONUtil.fromJSON(tokenJson, TokenResponse.class);
        System.out.println(tokenResponse);

        String appJson = app.sendSimple(JSONUtil.toJSON(new AppRequest(tokenResponse.getJwt(), "Dummy service request")), "service");
        AppResponse appResponse = JSONUtil.fromJSON(appJson, AppResponse.class);
        System.out.println(appResponse);
    }
}
