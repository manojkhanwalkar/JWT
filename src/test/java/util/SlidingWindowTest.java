/*
 * SlidingWindowTest
 *
 * Tests SlidingWindow: every chunk arrives exactly once and in order, and the worker
 * pool goes idle when done instead of hanging.
 */
package util;

import data.Chunk;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SlidingWindowTest {

    /** Delivers chunks 5..54 in order, then null, and the workers finish. */
    @Test(timeout = 10_000)
    public void deliversAllChunksInOrderAndTerminates() throws Exception {
        SlidingWindow window = new SlidingWindow(5, 55, 4);
        window.start();
        for (int expected = 5; expected < 55; expected++) {
            Chunk chunk = window.getSequence();
            assertEquals(expected, chunk.sequence);
        }
        assertNull(window.getSequence());

        window.service.shutdown();
        assertTrue("workers should be idle, not blocked", window.service.awaitTermination(2, TimeUnit.SECONDS));
    }
}
