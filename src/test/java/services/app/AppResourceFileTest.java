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
import jakarta.ws.rs.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import util.CheckSum;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class AppResourceFileTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private AppResource resource;
    private Path file;
    private byte[] bytes;

    /** Writes a 1000-byte file that includes non-ASCII bytes, so encoding mistakes show up. */
    @Before
    public void setUp() throws Exception {
        bytes = new byte[1000];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) i;
        }
        file = tmp.getRoot().toPath().resolve("f");
        Files.write(file, bytes);
        resource = new AppResource(tmp.getRoot().getPath());
    }

    /** /meta reports the right size and MD5. */
    @Test
    public void metaReportsSizeAndChecksum() {
        FileMetaResponse meta = resource.fileMeta(new FileMetaRequest("f", null));
        assertEquals(1000, meta.getFileSize());
        assertEquals(CheckSum.checkSum(file), meta.getCheckSum());
    }

    /** Chunks fetched in an uneven split rebuild the exact original bytes. A range past the end is clipped. */
    @Test
    public void chunksReassembleExactly() {
        StringBuilder sb = new StringBuilder();
        for (int start = 0; start < 1000; start += 333) {
            sb.append(resource.chunk(new ChunkRequest("f", null, start, start + 333)).getContents());
        }
        assertEquals(1000, sb.length());
        for (int i = 0; i < 1000; i++) {
            assertEquals((char) (bytes[i] & 0xff), sb.charAt(i));
        }
    }

    /** Names that try to leave the download directory are rejected. */
    @Test(expected = NotFoundException.class)
    public void rejectsPathTraversal() {
        resource.fileMeta(new FileMetaRequest("../../../../etc/passwd", null));
    }
}
