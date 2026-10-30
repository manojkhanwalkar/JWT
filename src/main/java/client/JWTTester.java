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
import util.Connection;
import util.JSONUtil;

public class JWTTester {

    /** Logs in and gets a token (TokenClient), then calls the service and prints the response. */
    public static void main(String[] args) throws Exception {
        Connection app = new Connection("https://localhost:8380/");

        String jwt = TokenClient.obtainToken();
        System.out.println("Token: " + jwt);

        String appJson = app.sendSimple(JSONUtil.toJSON(new AppRequest(jwt, "Dummy service request")), "service");
        AppResponse appResponse = JSONUtil.fromJSON(appJson, AppResponse.class);
        System.out.println(appResponse);
    }
}
