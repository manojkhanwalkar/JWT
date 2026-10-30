/*
 * TokenIssuer
 *
 * Swaps a login session id for a signed JWT. The session is checked with the
 * authenticator's /verify, and the token's subject is the session owner. Shared by
 * every service that exposes POST /token so the check lives in one place.
 */
package util;

import data.SessionVerifyRequest;
import data.SessionVerifyResponse;
import data.TokenRequest;
import data.TokenResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class TokenIssuer {

    private static final Logger LOG = LoggerFactory.getLogger(TokenIssuer.class);

    /** Authenticator service. Reused across requests: Connection shares one pooled HttpClient. */
    private final Connection login = new Connection("https://localhost:8480/");

    private final JWSSignUtil jwsSignUtil = new JWSSignUtil();

    /** Contents of /certificate never change, so they are serialized once. */
    private final String certificateJson = JSONUtil.toJSON(jwsSignUtil.getPublicKey());

    /**
     * Returns a signed JWT if the request's session is valid. If it is not valid, or
     * the authenticator cannot be reached, the response has status Invalid and a
     * random placeholder token.
     */
    public TokenResponse issue(TokenRequest request) {
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

    /** The JWT verification key (Base64 X.509 EC public key) as a JSON string. */
    public String certificateJson() {
        return certificateJson;
    }
}
