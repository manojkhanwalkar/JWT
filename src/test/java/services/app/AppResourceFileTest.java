/*
 * AppResourceFileTest
 *
 * Tests the file-download endpoints of AppResource (/meta and /chunk) against a
 * temporary directory. They are called directly, not over HTTP.
 */
package services.app;

import data.ChunkRequest;
import data.FileMetaRequest;
import data.FileMetaResponse;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import util.CheckSum;
import util.CryptUtil;
import util.JWSSignUtil;
import util.JWSVerifyUtil;

import java.security.KeyPair;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class AppResourceFileTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private AppResource resource;
    private Path file;
    private byte[] bytes;
    private String jwt;

    /** Writes a 1000-byte file that includes non-ASCII bytes, so encoding mistakes show up. */
    @Before
    public void setUp() throws Exception {
        bytes = new byte[1000];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) i;
        }
        file = tmp.getRoot().toPath().resolve("f");
        Files.write(file, bytes);
        KeyPair keys = CryptUtil.generateECKeyPair();
        jwt = new JWSSignUtil(keys.getPrivate(), keys.getPublic()).issueToken("user1");
        resource = new AppResource(tmp.getRoot().getPath(), new JWSVerifyUtil(keys.getPublic()));
    }

    /** /meta reports the right size and MD5. */
    @Test
    public void metaReportsSizeAndChecksum() {
        FileMetaResponse meta = resource.fileMeta(new FileMetaRequest("f", jwt));
        assertEquals(1000, meta.getFileSize());
        assertEquals(CheckSum.checkSum(file), meta.getCheckSum());
    }

    /** Chunks fetched in an uneven split rebuild the exact original bytes. A range past the end is clipped. */
    @Test
    public void chunksReassembleExactly() {
        StringBuilder sb = new StringBuilder();
        for (int start = 0; start < 1000; start += 333) {
            sb.append(resource.chunk(new ChunkRequest("f", jwt, start, start + 333)).getContents());
        }
        assertEquals(1000, sb.length());
        for (int i = 0; i < 1000; i++) {
            assertEquals((char) (bytes[i] & 0xff), sb.charAt(i));
        }
    }

    /** Names that try to leave the download directory are rejected. */
    @Test(expected = NotFoundException.class)
    public void rejectsPathTraversal() {
        resource.fileMeta(new FileMetaRequest("../../../../etc/passwd", jwt));
    }

    /** /meta without a token is rejected. */
    @Test(expected = NotAuthorizedException.class)
    public void metaRequiresToken() {
        resource.fileMeta(new FileMetaRequest("f", null));
    }

    /** /chunk with a token signed by a different key is rejected. */
    @Test(expected = NotAuthorizedException.class)
    public void chunkRejectsForeignToken() {
        KeyPair other = CryptUtil.generateECKeyPair();
        String forged = new JWSSignUtil(other.getPrivate(), other.getPublic()).issueToken("user1");
        resource.chunk(new ChunkRequest("f", forged, 0, 10));
    }
}
