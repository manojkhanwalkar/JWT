package util;

import data.Chunk;

import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

        service  = Executors.newFixedThreadPool(windowSize);
    }

     int nextSequence;
    int nextToRetrieve=0;

    public Chunk getSequence()
    {
        try {
            lock.lock();
            while (queue.isEmpty() || queue.peek().sequence != nextSequence) {
                try {

                //    System.out.println("Consumer Waiting for " + nextSequence);
                    notEmpty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Chunk chunk = queue.remove();
            nextSequence++;
            pending--;
            notFull.signalAll();

            return chunk;
        } finally {
            lock.unlock();
        }
    }


    public void addChunk(Chunk chunk)
    {
        try {
            lock.lock();
    /*        while (chunk.sequence!=nextSequence) {
         //   while (pending>=wi) {
            try {
                    System.out.println("Producer has " + chunk.sequence + " requires " + nextSequence);
                    notFull.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/

           // System.out.println("Producer added " + chunk.sequence);
            queue.add(chunk);

            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }


    public int getNextToRetrieve()
    {
        try {

            lock.lock();
               while (pending>=windowSize) {
                   try {
                         System.out.println(Thread.currentThread().getName() + " Waiting to clear pending" + pending + " " + windowSize);
                       notFull.await();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }

        }
            pending++;
            return nextToRetrieve++;
        } finally {
            lock.unlock();
        }
    }


    final int numTasks = 4;
    ExecutorService service;
    public void start() {



            for (int i = 0; i < numTasks; i++) {
                ChunkTask task = new ChunkTask(getNextToRetrieve(),this);
                service.submit(task);
            }

    }

    public void end() {

        service.shutdownNow();
    }


    static class ChunkTask implements Runnable
    {

        int sequence;
        SlidingWindow window;

        public ChunkTask(int sequence,SlidingWindow window)
        {
            this.sequence = sequence;
            this.window = window;
        }

        public void run()
        {


        /*   try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            window.addChunk(new Chunk(sequence));



            int next = window.getNextToRetrieve();

            if (next<window.end) {

                ChunkTask task = new ChunkTask(next, window);

                window.service.submit(task);
            }

        }
    }



}
