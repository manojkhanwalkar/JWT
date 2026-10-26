/*
 * AppResource
 *
 * REST endpoints of the app (resource) service:
 *   POST /service      Business call protected by a JWT. The token is checked against
 *                      the authorizer's public key.
 *   POST /meta         Size and MD5 checksum of a downloadable file (JWT required).
 *   POST /chunk        Byte range [start, end) of a downloadable file (JWT required).
 *   POST /keyexchange  Step 1 of ECDHE: trade public keys and get back a session id.
 *   POST /eservice     Step 2 of ECDHE: decrypt the request, upper-case it, and
 *                      return it encrypted.
 */
package services.app;

import com.codahale.metrics.annotation.Timed;
import data.AppRequest;
import data.AppResponse;
import data.ChunkRequest;
import data.ChunkResponse;
import data.EncryptedMessage;
import data.FileMetaRequest;
import data.FileMetaResponse;
import data.KeyExchangeRequest;
import data.KeyExchangeResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.core.MediaType;
import util.CheckSum;
import util.Connection;
import util.JSONUtil;
import util.JWSVerifyUtil;
import util.KeyExchangeManager;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class AppResource {

    private static final String AUTHORIZER_URL = "https://localhost:8280/";

    private final java.nio.file.Path filesDirectory;

    /** Cached MD5 per file. An entry is used only while the file's size and mtime still match. */
    private final ConcurrentMap<java.nio.file.Path, CachedChecksum> checksums = new ConcurrentHashMap<>();

    private volatile JWSVerifyUtil jwsVerifyUtil;

    private record CachedChecksum(long size, long modified, String md5) {
    }

    /**
     * @param filesDirectory directory that /meta and /chunk serve files from
     */
    public AppResource(String filesDirectory) {
        this(filesDirectory, null);
    }

    /**
     * @param filesDirectory directory that /meta and /chunk serve files from
     * @param verifier       token verifier to use; if null, the authorizer's key is fetched
     */
    AppResource(String filesDirectory, JWSVerifyUtil verifier) {
        this.filesDirectory = java.nio.file.Path.of(filesDirectory).toAbsolutePath().normalize();
        if (verifier != null) {
            jwsVerifyUtil = verifier;
            return;
        }
        try {
            jwsVerifyUtil = fetchVerifier();
        } catch (Exception e) {
            // The authorizer may not be up yet. verifier() tries again on the first /service call.
            System.err.println("Authorizer public key not available yet: " + e.getMessage());
        }
    }

    /** Downloads the authorizer's public key and builds a JWS verifier from it. */
    private static JWSVerifyUtil fetchVerifier() throws IOException, InterruptedException {
        String certJson = new Connection(AUTHORIZER_URL).get("certificate");
        return new JWSVerifyUtil(JSONUtil.fromJSON(certJson, String.class));
    }

    /** Returns the verifier. If startup could not reach the authorizer, it is fetched now. */
    private JWSVerifyUtil verifier() {
        JWSVerifyUtil verifier = jwsVerifyUtil;
        if (verifier == null) {
            synchronized (this) {
                verifier = jwsVerifyUtil;
                if (verifier == null) {
                    try {
                        verifier = jwsVerifyUtil = fetchVerifier();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        return verifier;
    }

    /**
     * Handles a JWT-protected service call. If the token is valid it returns the
     * request's payload in upper case, and otherwise it returns Status.Invalid.
     */
    @POST
    @Timed
    @Path("/service")
    public AppResponse claims(AppRequest request) {
        if (verifier().verifyJWS(request.getToken())) {
            String payload = request.getDummy() == null ? "" : request.getDummy().toUpperCase();
            return new AppResponse(AppResponse.Status.Valid, payload);
        }
        return new AppResponse(AppResponse.Status.Invalid, "");
    }

    /**
     * Returns a file's size and MD5 checksum so a client can plan a chunked download
     * and check the result. Checksums are cached until the file changes.
     */
    @POST
    @Timed
    @Path("/meta")
    public FileMetaResponse fileMeta(FileMetaRequest request) {
        requireValidToken(request.getJwt());
        java.nio.file.Path file = resolve(request.getFileName());
        try {
            long size = Files.size(file);
            long modified = Files.getLastModifiedTime(file).toMillis();
            CachedChecksum cached = checksums.get(file);
            if (cached == null || cached.size() != size || cached.modified() != modified) {
                cached = new CachedChecksum(size, modified, CheckSum.checkSum(file));
                checksums.put(file, cached);
            }
            return new FileMetaResponse(Math.toIntExact(size), cached.md5());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Returns bytes [start, end) of a file, clipped to the file's length. Bytes map
     * one-to-one to chars (ISO-8859-1), so any file comes through unchanged. The
     * read is positional, so the server never scans from the start of the file.
     */
    @POST
    @Timed
    @Path("/chunk")
    public ChunkResponse chunk(ChunkRequest request) {
        requireValidToken(request.getJwt());
        java.nio.file.Path file = resolve(request.getFileName());
        if (request.getStart() < 0 || request.getEnd() < request.getStart()) {
            throw new BadRequestException("Invalid range " + request.getStart() + "-" + request.getEnd());
        }
        try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
            long end = Math.min(request.getEnd(), channel.size());
            int length = (int) Math.max(0, end - request.getStart());
            ByteBuffer buffer = ByteBuffer.allocate(length);
            long position = request.getStart();
            while (buffer.hasRemaining()) {
                int read = channel.read(buffer, position);
                if (read < 0) {
                    break;
                }
                position += read;
            }
            buffer.flip();
            return new ChunkResponse(StandardCharsets.ISO_8859_1.decode(buffer).toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** ECDHE step 1: generate a server key pair, derive the session key, and return our public key and the session id. */
    @POST
    @Timed
    @Path("/keyexchange")
    public KeyExchangeResponse keyExchange(KeyExchangeRequest request) {
        return new KeyExchangeManager().processExchange(request);
    }

    /** ECDHE step 2: decrypt the request, upper-case it, and return it encrypted under the same session key. */
    @POST
    @Timed
    @Path("/eservice")
    public EncryptedMessage encryptedService(EncryptedMessage request) {
        String requestStr = KeyExchangeManager.decryptRequest(request);
        String responseStr = JSONUtil.toJSON(requestStr.toUpperCase());
        return KeyExchangeManager.encryptResponse(responseStr, request);
    }

    /**
     * Rejects the request with 401 unless {@code jwt} is a valid token from the
     * authorizer, or with 503 if the authorizer's key cannot be fetched (fail closed).
     */
    private void requireValidToken(String jwt) {
        JWSVerifyUtil verifier;
        try {
            verifier = verifier();
        } catch (RuntimeException e) {
            throw new ServiceUnavailableException("Token verification key unavailable");
        }
        if (!verifier.verifyJWS(jwt)) {
            throw new NotAuthorizedException("Valid JWT required", "Bearer");
        }
    }

    /**
     * Resolves a client-supplied file name inside the files directory. Names that
     * try to escape it (for example "../../etc/passwd") are rejected.
     *
     * @throws NotFoundException if the name is invalid or the file does not exist
     */
    private java.nio.file.Path resolve(String fileName) {
        if (fileName == null) {
            throw new NotFoundException();
        }
        java.nio.file.Path file = filesDirectory.resolve(fileName).normalize();
        if (!file.startsWith(filesDirectory) || !Files.isRegularFile(file)) {
            throw new NotFoundException("No such file: " + fileName);
        }
        return file;
    }
}
