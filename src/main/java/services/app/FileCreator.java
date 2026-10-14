/*
 * FileCreator
 *
 * Dev utility that fills the app service's download directory with random
 * lowercase-ASCII test files (File1 = 10 KB, File2 = 100 KB, File3 = 1 MB) and
 * prints the MD5 of each. Run it once before trying the download clients.
 */
package services.app;

import util.CheckSum;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

public class FileCreator {

    private final Path dir;

    /** @param dir directory the files are written to. It is created if it is missing. */
    public FileCreator(String dir) {
        this.dir = Path.of(dir);
    }

    /** Creates the three standard test files in the default download directory. */
    public static void main(String[] args) {
        FileCreator fileCreator = new FileCreator(args.length > 0 ? args[0] : AppConfiguration.DEFAULT_FILES_DIRECTORY);

        fileCreator.createFile("File1", 10_000);
        fileCreator.createFile("File2", 100_000);
        fileCreator.createFile("File3", 1_000_000);
    }

    /** Writes {@code size} random 'a'-'z' bytes to {@code dir/name} and prints the file's MD5. */
    public void createFile(String name, int size) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Path file = dir.resolve(name);

        try {
            Files.createDirectories(dir);
            try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.US_ASCII)) {
                for (int i = 0; i < size; i++) {
                    writer.write('a' + random.nextInt(26));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        System.out.println(name + " " + CheckSum.checkSum(file));
    }
}
