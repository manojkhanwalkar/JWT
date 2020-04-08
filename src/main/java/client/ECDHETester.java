package client;

import data.*;
import util.Connection;
import util.JSONUtil;

public class ECDHETester {

    public static void main(String[] args) throws Exception{


        Connection  app = new Connection("https://localhost:8380/");

        String str = app.esend("Hello World","eservice");

        System.out.println(str);




    }

}
