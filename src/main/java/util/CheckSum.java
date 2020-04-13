package util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class CheckSum {



    public static String checkSum(String file){
        String checksum = null;
        try {
            checksum = DigestUtils.md5Hex(new FileInputStream(file));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return checksum;
    }
}
