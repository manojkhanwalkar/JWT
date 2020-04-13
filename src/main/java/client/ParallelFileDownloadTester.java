package client;

import data.ChunkRequest;
import data.ChunkResponse;
import data.FileMetaRequest;
import data.FileMetaResponse;
import util.CheckSum;
import util.Connection;
import util.JSONUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelFileDownloadTester {

    static class ChunkTask implements Runnable {

        volatile String[] responses;
        int index;
        int start ;
        int end;
        CountDownLatch latch;
        String fileName;

        Connection app = new Connection("https://localhost:8380/");

        public ChunkTask(String[] responses, int index, int start, int end, CountDownLatch latch,String fileName) {
            this.responses = responses;
            this.index = index;
            this.start = start;
            this.end = end;
            this.latch = latch;
            this.fileName = fileName;
        }

        public void run()
        {
            ChunkRequest request = new ChunkRequest(fileName, "TODO", start, end);
            String str = null;
            try {

                System.out.println(Thread.currentThread().getName() + " " + start + " " + end);
                str = app.sendSimple(JSONUtil.toJSON(request), "chunk");
               // Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ChunkResponse response = (ChunkResponse) JSONUtil.fromJSON(str, ChunkResponse.class);
            responses[index]= response.getContents();
            latch.countDown();
        }
    }

    public static void main(String[] args) throws Exception  {

        String fileName = "File3";

        Connection app = new Connection("https://localhost:8380/");

        FileMetaRequest metaRequest = new FileMetaRequest(fileName,"TODO");

        String str = app.sendSimple(JSONUtil.toJSON(metaRequest),"meta");

        FileMetaResponse metaResponse = (FileMetaResponse)JSONUtil.fromJSON(str,FileMetaResponse.class);

        System.out.println(str);

        final int chunks=100;

        final int chunkSize = metaResponse.getFileSize()/chunks;

        int start =0; int end = chunkSize;

        File file = new File("/tmp/"+metaRequest.getFileName());

         String[] responses = new String[chunks];

         CountDownLatch latch = new CountDownLatch(chunks);

        ExecutorService service = Executors.newFixedThreadPool(4);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (int i = 0; i < chunks; i++) {

                ChunkTask chunkTask = new ChunkTask(responses, i, start, end, latch, fileName);


                service.submit(chunkTask);
                start = end;
                end = end+chunkSize;

            }


            latch.await();

            for (int i = 0; i < chunks; i++) {

                writer.write(responses[i]);
            }

            writer.flush();

        }

        String checkSum = CheckSum.checkSum("/tmp/"+file.getName());

       // System.out.println(checkSum);


        System.out.println("Check sum match is " + checkSum.equals(metaResponse.getCheckSum()));

        service.shutdownNow();

    }



}
