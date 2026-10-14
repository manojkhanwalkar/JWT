/*
 * ChunkRequest
 *
 * Body of POST /chunk: asks for bytes [start, end) of a file in the app service's
 * download directory.
 */
package data;

public class ChunkRequest {

    private String fileName;

    private String jwt;

    private int start;

    private int end;

    /** Creates a request for bytes [start, end) of {@code fileName}. */
    public ChunkRequest(String fileName, String jwt, int start, int end) {
        this.fileName = fileName;
        this.jwt = jwt;
        this.start = start;
        this.end = end;
    }

    /** No-arg constructor for Jackson. */
    public ChunkRequest() {
    }

    /** File name, relative to the download directory. */
    public String getFileName() {
        return fileName;
    }

    /** Sets the file name. */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /** JWT authorizing the download. Not checked yet. */
    public String getJwt() {
        return jwt;
    }

    /** Sets the JWT. */
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    /** First byte offset (inclusive). */
    public int getStart() {
        return start;
    }

    /** Sets the first byte offset. */
    public void setStart(int start) {
        this.start = start;
    }

    /** End byte offset (exclusive). */
    public int getEnd() {
        return end;
    }

    /** Sets the end byte offset. */
    public void setEnd(int end) {
        this.end = end;
    }
}
