/*
 * SlidingWindow
 *
 * Prototype of an ordered, flow-controlled parallel fetcher, the building block for
 * a "sliding window" file download:
 *   - Worker threads fetch chunks with sequence numbers in [start, end) in parallel,
 *     and they may finish out of order.
 *   - No more than windowSize chunks can be fetched but not yet consumed at once.
 *     That caps memory use.
 *   - The consumer calls getSequence(), which always returns the chunks strictly in
 *     sequence order and blocks until the next one is ready.
 *
 * A ChunkTask stands in for the fetch. A real download would make an HTTP /chunk call there.
 */
package util;

import data.Chunk;

import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindow {

    /** Returned by {@link #getNextToRetrieve()} when every sequence has been handed out. */
    static final int NO_MORE = -1;

    final int start;
    final int end;
    final int windowSize;

    private final PriorityQueue<Chunk> queue = new PriorityQueue<>();

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    /** Number of chunks handed to a worker but not yet consumed. */
    private int pending = 0;
    /** Next sequence the consumer is waiting for. */
    private int nextSequence;
    /** Next sequence to give to a worker. */
    private int nextToRetrieve;

    final ExecutorService service;

    /** Creates a window over sequences [start, end) with at most {@code windowSize} chunks in flight. */
    public SlidingWindow(int start, int end, int windowSize) {
        if (windowSize <= 0) {
            throw new IllegalArgumentException("windowSize must be positive");
        }
        this.start = start;
        this.end = end;
        this.windowSize = windowSize;
        this.nextSequence = start;
        this.nextToRetrieve = start;
        this.service = Executors.newFixedThreadPool(windowSize);
    }

    /**
     * Blocks until the next chunk in order is available, and then returns it. Taking
     * a chunk frees a window slot, which wakes a waiting worker.
     *
     * @return the next chunk, or null if every chunk has been consumed
     * @throws InterruptedException if interrupted while waiting
     */
    public Chunk getSequence() throws InterruptedException {
        lock.lock();
        try {
            if (nextSequence >= end) {
                return null;
            }
            while (queue.isEmpty() || queue.peek().sequence != nextSequence) {
                notEmpty.await();
            }
            Chunk chunk = queue.remove();
            nextSequence++;
            pending--;
            notFull.signal();
            return chunk;
        } finally {
            lock.unlock();
        }
    }

    /** Called by a worker when a chunk has been fetched. It wakes the consumer. */
    public void addChunk(Chunk chunk) {
        lock.lock();
        try {
            queue.add(chunk);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Claims the next sequence number to fetch. Blocks while the window is full.
     *
     * @return the claimed sequence, or {@link #NO_MORE} once all of [start, end) has been
     *         handed out. Before this fix, the method kept claiming slots after the end
     *         and blocked forever, so the pool threads were stuck and the JVM never exited.
     * @throws InterruptedException if interrupted while waiting
     */
    public int getNextToRetrieve() throws InterruptedException {
        lock.lock();
        try {
            while (nextToRetrieve < end && pending >= windowSize) {
                notFull.await();
            }
            if (nextToRetrieve >= end) {
                return NO_MORE;
            }
            pending++;
            return nextToRetrieve++;
        } finally {
            lock.unlock();
        }
    }

    /** Starts one fetch task per window slot. Each task schedules the next fetch when it finishes. */
    public void start() {
        int initial = Math.min(windowSize, end - start);
        for (int i = 0; i < initial; i++) {
            service.submit(new ChunkTask(this));
        }
    }

    /** Stops all workers immediately. */
    public void end() {
        service.shutdownNow();
    }

    /**
     * Fetches one chunk and then resubmits itself for the next one. Because a task
     * resubmits itself instead of looping, each pool thread is free between fetches.
     */
    static class ChunkTask implements Runnable {

        private final SlidingWindow window;

        ChunkTask(SlidingWindow window) {
            this.window = window;
        }

        /** Claims a sequence, "fetches" it, and then queues the follow-up task. */
        @Override
        public void run() {
            try {
                int sequence = window.getNextToRetrieve();
                if (sequence == NO_MORE) {
                    return;
                }
                window.addChunk(new Chunk(sequence));
                window.service.submit(new ChunkTask(window));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RejectedExecutionException e) {
                // end() was called. The window is shutting down.
            }
        }
    }
}
