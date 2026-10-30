/*
 * FileDownloadTester
 *
 * Manual test of a sequential chunked download from the app service. It asks /meta
 * for the size and MD5, fetches the file in 10 sequential /chunk calls, writes it to
 * /tmp, and checks that the MD5 matches.
 */
package client;

import data.ChunkRequest;
import data.ChunkResponse;
import data.FileMetaRequest;
import data.FileMetaResponse;
import util.CheckSum;
import util.Connection;
import util.JSONUtil;

import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDownloadTester {

    private static final int CHUNKS = 10;

    /** Downloads File2 in order and reports whether the checksum matches. */
    public static void main(String[] args) throws Exception {
        String fileName = args.length > 0 ? args[0] : "File2";

        Connection app = new Connection("https://localhost:8380/");

        String metaJson = app.sendSimple(JSONUtil.toJSON(new FileMetaRequest(fileName, TokenClient.currentToken())), "meta");
        FileMetaResponse metaResponse = JSONUtil.fromJSON(metaJson, FileMetaResponse.class);
        System.out.println(metaJson);

        int fileSize = metaResponse.getFileSize();
        int chunkSize = Math.ceilDiv(fileSize, CHUNKS);
        Path file = Path.of("/tmp", fileName);

        // ISO-8859-1 maps each char back to the byte the server read, so the copy is exact.
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.ISO_8859_1)) {
            for (int start = 0; start < fileSize; start += chunkSize) {
                int end = Math.min(start + chunkSize, fileSize);
                String chunkJson = app.sendSimple(JSONUtil.toJSON(new ChunkRequest(fileName, TokenClient.currentToken(), start, end)), "chunk");
                writer.write(JSONUtil.fromJSON(chunkJson, ChunkResponse.class).getContents());
            }
        }

        System.out.println("Checksum match is " + CheckSum.checkSum(file).equals(metaResponse.getCheckSum()));
    }
}
