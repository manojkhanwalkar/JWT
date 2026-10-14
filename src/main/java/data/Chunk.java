/*
 * Chunk
 *
 * One piece of a sliding-window download, identified by its sequence number. Chunks
 * sort by sequence, so a PriorityQueue releases them in order.
 */
package data;

public class Chunk implements Comparable<Chunk> {

    public final int sequence;

    /** Creates the chunk with the given sequence number. */
    public Chunk(int sequence) {
        this.sequence = sequence;
    }

    /** Orders chunks by sequence number. */
    @Override
    public int compareTo(Chunk chunk) {
        return Integer.compare(this.sequence, chunk.sequence);
    }

    @Override
    public String toString() {
        return "Chunk{sequence=" + sequence + '}';
    }
}
