/*
 * ChunkResponse
 *
 * Reply from POST /chunk: the requested bytes, one char per byte (ISO-8859-1).
 */
package data;

public class ChunkResponse {

    private String contents;

    /** No-arg constructor for Jackson. */
    public ChunkResponse() {
    }

    /** Creates a response that carries the given chunk contents. */
    public ChunkResponse(String contents) {
        this.contents = contents;
    }

    /** Returns the chunk contents. */
    public String getContents() {
        return contents;
    }

    /** Sets the chunk contents. */
    public void setContents(String contents) {
        this.contents = contents;
    }
}
