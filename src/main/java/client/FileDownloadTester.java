package client;

import data.FileMetaRequest;
import util.Connection;
import util.JSONUtil;

public class FileDownloadTester {

    public static void main(String[] args) throws Exception  {

        Connection app = new Connection("https://localhost:8380/");

        FileMetaRequest metaRequest = new FileMetaRequest("File1","TODO");

        String str = app.sendSimple(JSONUtil.toJSON(metaRequest),"meta");

        System.out.println(str);



    }



}
