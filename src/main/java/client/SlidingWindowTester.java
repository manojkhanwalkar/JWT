package client;

import data.Chunk;
import util.SlidingWindow;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SlidingWindowTester {

    public static void main(String[] args) throws Exception {



        SlidingWindow window = new SlidingWindow(0,100,10);


        window.start();

        for (int i=0;i<100;i++)
        {
            System.out.println(window.getSequence());

              Thread.sleep(2000);
        }

        window.end();













    }


}
