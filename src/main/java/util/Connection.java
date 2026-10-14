/*
 * Connection
 *
 * Small HTTPS client that the test clients and the services use to call each
 * other. It supports:
 *   - plain JSON POST and GET against a base URL
 *   - esend(): a full ECDHE round trip. It runs a key exchange with the server,
 *     sends an AES-encrypted message, and decrypts the encrypted reply.
 *
 * Every Connection shares one HttpClient. HttpClient is thread-safe, and each new
 * one starts its own selector thread and SSL context, so the old pattern of one
 * client per Connection (and per request in some callers) was expensive.
 *
 * Hostname verification is turned off because the services use a self-signed
 * localhost certificate. Only do this in development.
 */
package util;

import data.EncryptedMessage;
import data.KeyExchangeRequest;
import data.KeyExchangeResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyPair;
import java.time.Duration;
import java.util.Base64;

public class Connection {

    private static final Duration TIMEOUT = Duration.ofMinutes(1);

    private static final HttpClient CLIENT = createClient();

    private final String target;

    /** @param target base URL that ends in '/', for example {@code https://localhost:8380/} */
    public Connection(String target) {
        this.target = target;
    }

    /** Must run before the first HttpClient is built, which is why it is called from the static initializer. */
    private static HttpClient createClient() {
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
        return HttpClient.newBuilder().connectTimeout(TIMEOUT).build();
    }

    /** POSTs a JSON body to {@code target + action} and returns the response body. */
    public String sendSimple(String request, String action) throws IOException, InterruptedException {
        return post(request, action);
    }

    /** GETs {@code target + url} and returns the response body. */
    public String get(String url) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(target + url))
                .GET()
                .timeout(TIMEOUT)
                .build();
        return send(httpRequest);
    }

    /**
     * Sends {@code message} to {@code eservice} encrypted end to end:
     *   1. Make an ephemeral EC key pair and POST the public key to /keyexchange.
     *   2. Derive the shared AES key from the server's public key.
     *   3. Encrypt the message with a random IV and POST it with the session id.
     *   4. Decrypt the server's encrypted reply.
     *
     * @return the decrypted response body
     */
    public String esend(String message, String eservice) throws IOException, InterruptedException {
        KeyPair keyPair = CryptUtil.generateECKeyPair();

        KeyExchangeRequest request = new KeyExchangeRequest(CryptUtil.convertPublicKeyToString(keyPair.getPublic()));
        KeyExchangeResponse response = JSONUtil.fromJSON(post(JSONUtil.toJSON(request), "keyexchange"), KeyExchangeResponse.class);

        DHUtil util = new DHUtil(keyPair.getPublic(), keyPair.getPrivate());
        util.setReceiverPublicKey(CryptUtil.convertStringtoPublicKey(response.getPublicKey(), "EC"));

        // Key exchange is done. Send the encrypted message and get the encrypted response.
        byte[] iv = DHUtil.newIv();
        EncryptedMessage encryptedMessage = new EncryptedMessage();
        encryptedMessage.setMessage(util.encrypt(message, iv));
        encryptedMessage.setSessionId(response.getSessionId());
        encryptedMessage.setIv(Base64.getEncoder().encodeToString(iv));

        EncryptedMessage encryptedResponse = JSONUtil.fromJSON(post(JSONUtil.toJSON(encryptedMessage), eservice), EncryptedMessage.class);
        return util.decrypt(encryptedResponse.getMessage(), encryptedResponse.getIv());
    }

    private String post(String body, String url) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(target + url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(TIMEOUT)
                .build();
        return send(httpRequest);
    }

    /**
     * Runs the request and returns the body.
     *
     * @throws IOException if the server returns a non-2xx status. Before this, the
     *         error page was handed back as if it were a valid JSON reply.
     */
    private static String send(HttpRequest httpRequest) throws IOException, InterruptedException {
        HttpResponse<String> response = CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 != 2) {
            throw new IOException("HTTP " + response.statusCode() + " from " + httpRequest.uri() + ": " + response.body());
        }
        return response.body();
    }
}
