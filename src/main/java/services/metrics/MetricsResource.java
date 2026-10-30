/*
 * MetricsResource
 *
 * REST resource of the metrics service. This is work in progress: for now it
 * exposes the same /token and /certificate endpoints as the authorizer's
 * AuthResource. The metrics.data model and the config classes in
 * services.metrics.config are the start of a collector that polls each service's
 * Dropwizard admin /metrics endpoint.
 */
package services.metrics;

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
public class MetricsResource {

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
