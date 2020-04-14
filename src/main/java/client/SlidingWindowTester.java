package client;

import data.Chunk;
import util.SlidingWindow;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SlidingWindowTester {

    public static void main(String[] args) throws Exception {



        SlidingWindow window = new SlidingWindow(0,100,10);





        Thread producer = new Thread(()->{

            Set<Integer> numbers = new HashSet<>();

            Random random = new Random();

            int start=0;

            for (int i=0;i<10;i++)
            {
                int end = 10*(i+1);
                int count=0;
                while(count<10)
                {

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int x = random.nextInt(100);
                    if (start<=x && x<end && !numbers.contains(x)) {

                        window.addChunk(new Chunk(x));

                        numbers.add(x);


                       // System.out.println("Added " + x);

                        count++;
                    }
                }
                start=end;

            }



            System.out.println("Completed adding chunks");


        });

        producer.start();


        for (int i=0;i<100;i++)
        {
            System.out.println(window.getSequence());

          //  Thread.sleep(100);
        }





    }


}
