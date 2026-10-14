/*
 * ParallelFileDownloadTester
 *
 * Manual test of a parallel chunked download from the app service. It splits the
 * file into 100 ranges, fetches them at the same time on a 4-thread pool, puts them
 * back together in order, writes the file to /tmp, and checks the MD5 against /meta.
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelFileDownloadTester {

    private static final int CHUNKS = 100;
    private static final int THREADS = 4;

    /** Thread-safe, so one instance serves every download task. */
    private static final Connection APP = new Connection("https://localhost:8380/");

    /** Fetches bytes [start, end) of {@code fileName} from /chunk. */
    static String fetchChunk(String fileName, int start, int end) {
        try {
            System.out.println(Thread.currentThread().getName() + " " + start + " " + end);
            String json = APP.sendSimple(JSONUtil.toJSON(new ChunkRequest(fileName, "TODO", start, end)), "chunk");
            return JSONUtil.fromJSON(json, ChunkResponse.class).getContents();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CompletionException(e);
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    /** Downloads File1 in parallel and reports whether the checksum matches. */
    public static void main(String[] args) throws Exception {
        String fileName = args.length > 0 ? args[0] : "File1";

        String metaJson = APP.sendSimple(JSONUtil.toJSON(new FileMetaRequest(fileName, "TODO")), "meta");
        FileMetaResponse metaResponse = JSONUtil.fromJSON(metaJson, FileMetaResponse.class);
        System.out.println(metaJson);

        int fileSize = metaResponse.getFileSize();
        int chunkSize = Math.max(1, Math.ceilDiv(fileSize, CHUNKS));
        Path file = Path.of("/tmp", fileName);

        ExecutorService service = Executors.newFixedThreadPool(THREADS);
        try {
            List<CompletableFuture<String>> chunks = new ArrayList<>();
            for (int start = 0; start < fileSize; start += chunkSize) {
                final int s = start, e = Math.min(start + chunkSize, fileSize);
                chunks.add(CompletableFuture.supplyAsync(() -> fetchChunk(fileName, s, e), service));
            }

            // The futures are joined in submission order, so the chunks are written in file order.
            // If any chunk fails, join() throws, which is better than hanging the way the old latch did.
            try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.ISO_8859_1)) {
                for (CompletableFuture<String> chunk : chunks) {
                    writer.write(chunk.join());
                }
            }
        } finally {
            service.shutdownNow();
        }

        System.out.println("Checksum match is " + CheckSum.checkSum(file).equals(metaResponse.getCheckSum()));
    }
}
