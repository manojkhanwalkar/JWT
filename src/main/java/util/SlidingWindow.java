package util;

import data.Chunk;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindow {

    int start;
    int end;
    int windowSize;


    PriorityQueue<Chunk> queue;

    Lock lock = new ReentrantLock();

    Condition notFull ;
    Condition notEmpty;

    int pending=0;

    public SlidingWindow(int start, int end, int windowSize) {
        this.start = start;
        this.end = end;
        this.windowSize = windowSize;
        queue = new PriorityQueue<>();

        notFull = lock.newCondition();

        notEmpty = lock.newCondition();
        nextSequence=0;
    }

     int nextSequence;

    public Chunk getSequence()
    {
        try {
            lock.lock();
            while (queue.isEmpty() || queue.peek().sequence != nextSequence) {
                try {
                    notEmpty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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


    public void addChunk(Chunk chunk)
    {
        try {
            lock.lock();
            while (pending >= windowSize) {
                try {
                    notFull.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            queue.add(chunk);
            pending++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }


}
