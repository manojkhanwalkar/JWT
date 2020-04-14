package data;

import util.SlidingWindow;

public class Chunk implements Comparable<Chunk>
{
    public int sequence;

    public Chunk(int sequence) {
        this.sequence = sequence;
    }


    @Override
    public int compareTo(Chunk chunk) {
        return Integer.compare(this.sequence,chunk.sequence);
    }


    @Override
    public String toString() {
        return "Chunk{" +
                "sequence=" + sequence +
                '}';
    }
}
