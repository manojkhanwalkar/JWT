/*
 * SlidingWindowTester
 *
 * Manual demo of util.SlidingWindow. Workers fetch 100 simulated chunks with at most
 * 10 in flight, and a slow consumer (one chunk every 2 s) prints them strictly in
 * sequence order.
 */
package client;

import data.Chunk;
import util.SlidingWindow;

public class SlidingWindowTester {

    /** Consumes every chunk in order, and then shuts the window down. */
    public static void main(String[] args) throws Exception {
        SlidingWindow window = new SlidingWindow(0, 100, 10);
        window.start();

        Chunk chunk;
        while ((chunk = window.getSequence()) != null) {
            System.out.println(chunk);
            Thread.sleep(2000);
        }

        window.end();
    }
}
