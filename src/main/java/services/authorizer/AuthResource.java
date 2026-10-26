/*
 * AuthResource
 *
 * REST endpoints of the authorizer service:
 *   POST /token        swap a valid login session id for an ES256-signed JWT
 *   GET  /certificate  the public key that resource servers use to verify those JWTs
 */
package services.authorizer;

import com.codahale.metrics.annotation.Timed;
import data.SessionVerifyRequest;
import data.SessionVerifyResponse;
import data.TokenRequest;
import data.TokenResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import util.Connection;
import util.JSONUtil;
import util.JWSSignUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private static final Logger LOG = LoggerFactory.getLogger(AuthResource.class);

    /** Authenticator service. Reused across requests: Connection shares one pooled HttpClient. */
    private final Connection login = new Connection("https://localhost:8480/");

    private final JWSSignUtil jwsSignUtil = new JWSSignUtil();

    /** Contents of /certificate never change, so they are serialized once. */
    private final String certificateJson = JSONUtil.toJSON(jwsSignUtil.getPublicKey());

    /**
     * Swaps a session id for a signed JWT. The session is checked with the
     * authenticator's /verify. If it is not valid, or the authenticator cannot be
     * reached, the response has status Invalid and a random placeholder token.
     */
    @POST
    @Timed
    @Path("/token")
    public TokenResponse claims(TokenRequest request) {
        try {
            String verifyJson = login.sendSimple(JSONUtil.toJSON(new SessionVerifyRequest(request.getSessionId())), "verify");
            SessionVerifyResponse verifyResponse = JSONUtil.fromJSON(verifyJson, SessionVerifyResponse.class);

            if (verifyResponse.getStatus() == SessionVerifyResponse.Status.Valid && verifyResponse.getUserid() != null) {
                // Short-lived token bound to the user, issuer and audience (see JWSSignUtil#issueToken).
                return new TokenResponse(TokenResponse.Status.Valid, jwsSignUtil.issueToken(verifyResponse.getUserid()));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOG.warn("Token request failed: {}", e.toString());
        }
        return new TokenResponse(TokenResponse.Status.Invalid, UUID.randomUUID().toString());
    }

    /** Publishes the JWT verification key (Base64 X.509 EC public key) as a JSON string. */
    @GET
    @Timed
    @Path("/certificate")
    public String claims() {
        return certificateJson;
    }
}
