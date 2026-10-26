/*
 * TokenClient
 *
 * Helper for the manual test clients: logs in to the authenticator (:8480) and
 * trades the session for a JWT at the authorizer (:8280). The credentials default
 * to the seeded demo user and can be overridden with -Djwt.user / -Djwt.password.
 */
package client;

import data.LoginRequest;
import data.LoginResponse;
import data.TokenRequest;
import data.TokenResponse;
import util.Connection;
import util.JSONUtil;

import java.io.IOException;

public final class TokenClient {

    private TokenClient() {
    }

    /** Logs in and returns a JWT for the configured demo user. */
    public static String obtainToken() throws IOException, InterruptedException {
        return obtainToken(System.getProperty("jwt.user", "user1"), System.getProperty("jwt.password", "password1"));
    }

    /** Logs in as {@code userid} and returns a JWT. */
    public static String obtainToken(String userid, String password) throws IOException, InterruptedException {
        Connection login = new Connection("https://localhost:8480/");
        Connection auth = new Connection("https://localhost:8280/");

        LoginResponse loginResponse = JSONUtil.fromJSON(
                login.sendSimple(JSONUtil.toJSON(new LoginRequest(userid, password)), "login"), LoginResponse.class);
        if (loginResponse.getStatus() != LoginResponse.Status.sucess) {
            throw new IOException("Login failed for " + userid);
        }
        TokenResponse tokenResponse = JSONUtil.fromJSON(
                auth.sendSimple(JSONUtil.toJSON(new TokenRequest(loginResponse.getSessionId())), "token"), TokenResponse.class);
        if (tokenResponse.getStatus() != TokenResponse.Status.Valid) {
            throw new IOException("Token request failed for " + userid);
        }
        return tokenResponse.getJwt();
    }
}
