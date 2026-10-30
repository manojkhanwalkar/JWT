/*
 * AuthResource
 *
 * REST endpoints of the authorizer service:
 *   POST /token        swap a valid login session id for an ES256-signed JWT
 *   GET  /certificate  the public key that resource servers use to verify those JWTs
 */
package services.authorizer;

import com.codahale.metrics.annotation.Timed;
import data.TokenRequest;
import data.TokenResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import util.TokenIssuer;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final TokenIssuer tokenIssuer = new TokenIssuer();

    /** Swaps a session id for a signed JWT (see TokenIssuer#issue). */
    @POST
    @Timed
    @Path("/token")
    public TokenResponse claims(TokenRequest request) {
        return tokenIssuer.issue(request);
    }

    /** Publishes the JWT verification key (Base64 X.509 EC public key) as a JSON string. */
    @GET
    @Timed
    @Path("/certificate")
    public String claims() {
        return tokenIssuer.certificateJson();
    }
}
