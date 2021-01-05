import services.metrics.CircularQueue;

import java.util.Arrays;

public class CircularQueueTester {

    public static void main(String[] args) throws Exception {

        CircularQueue<Long> time = new CircularQueue<>(10, Long.MIN_VALUE);

        for (int i=0;i<2;i++)
        {
            time.add(System.currentTimeMillis());
            Thread.sleep(100);
        }

        Object[] metrics = time.get();

        for (Object obj : metrics)
        {
            System.out.println((Long)obj);
        }

    }
}
