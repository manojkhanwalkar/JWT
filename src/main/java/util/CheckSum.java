/*
 * CheckSum
 *
 * Computes MD5 checksums of files. The file-download flow uses it so a client can
 * check that the file it put together from downloaded chunks is byte-for-byte the
 * same as the server's copy.
 */
package util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CheckSum {

    private CheckSum() {
    }

    /**
     * Returns the hex-encoded MD5 digest of the given file. The file is streamed,
     * so it is never held in memory all at once.
     *
     * @throws UncheckedIOException if the file cannot be read
     */
    public static String checkSum(String file) {
        return checkSum(Path.of(file));
    }

    /** Path-based overload of {@link #checkSum(String)}. */
    public static String checkSum(Path file) {
        try (InputStream in = Files.newInputStream(file)) {
            return DigestUtils.md5Hex(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to checksum " + file, e);
        }
    }
}
