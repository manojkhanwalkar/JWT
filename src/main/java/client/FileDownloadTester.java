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

public class FileDownloadTester {

    public static void main(String[] args) throws Exception  {

        String fileName = "File2";

        Connection app = new Connection("https://localhost:8380/");

        FileMetaRequest metaRequest = new FileMetaRequest(fileName,"TODO");

        String str = app.sendSimple(JSONUtil.toJSON(metaRequest),"meta");

        FileMetaResponse metaResponse = (FileMetaResponse)JSONUtil.fromJSON(str,FileMetaResponse.class);

        System.out.println(str);

        final int chunks=10;

        final int chunkSize = metaResponse.getFileSize()/chunks;

        int start =0; int end = chunkSize;

        File file = new File("/tmp/"+metaRequest.getFileName());

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (int i = 0; i < chunks; i++) {

                ChunkRequest request = new ChunkRequest(fileName, "TODO", start, end);


                str = app.sendSimple(JSONUtil.toJSON(request), "chunk");

                ChunkResponse response = (ChunkResponse) JSONUtil.fromJSON(str, ChunkResponse.class);

                // System.out.println(str.length());


                writer.write(response.getContents());

                start = end;
                end = end+chunkSize;


            }

            writer.flush();

        }

        String checkSum = CheckSum.checkSum("/tmp/"+file.getName());

       // System.out.println(checkSum);


        System.out.println("Check match is " + checkSum.equals(metaResponse.getCheckSum()));

    }



}
