/*
 * CircularQueueTest
 *
 * Unit tests for CircularQueue: ordering (newest first), wrap-around overwrite, and
 * placeholder values in slots that have not been written.
 */
package services.metrics;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class CircularQueueTest {

    /** A partly filled queue returns the newest values first, followed by placeholders. */
    @Test
    public void partiallyFilledReturnsNewestFirstThenPlaceholders() {
        CircularQueue<Long> q = new CircularQueue<>(4, -1L);
        q.add(1L);
        q.add(2L);
        assertArrayEquals(new Object[]{2L, 1L, -1L, -1L}, q.get());
    }

    /** Once the queue is full, the oldest values are overwritten. */
    @Test
    public void wrapsAroundAndOverwritesOldest() {
        CircularQueue<Long> q = new CircularQueue<>(3, -1L);
        for (long i = 1; i <= 5; i++) {
            q.add(i);
        }
        assertArrayEquals(new Object[]{5L, 4L, 3L}, q.get());
    }
}
