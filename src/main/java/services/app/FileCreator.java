package services.app;

import org.apache.commons.codec.digest.DigestUtils;
import util.CheckSum;

import java.io.*;
import java.util.Random;
import java.util.logging.Level;

public class FileCreator {


    private static final String dir = "/home/manoj/IdeaProjects/JWT/src/main/resources/files/";
    public static void main(String[] args) {


        FileCreator fileCreator = new FileCreator();

        fileCreator.createFile("File1",10000);
        fileCreator.createFile("File2",100000);
        fileCreator.createFile("File3",1000000);

    }


    public void createFile(String name, int size)
    {
        Random random = new Random();

        File file = new File(dir+name);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (int i = 0; i < size; i++) {


                writer.append((char) ('a' + random.nextInt(26)));

            }

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


        String checkSum = CheckSum.checkSum(dir+name);
        System.out.println(checkSum);
    }





}
