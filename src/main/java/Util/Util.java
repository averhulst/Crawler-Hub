package Util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public synchronized static String toSha256(String str){
        str = str.toLowerCase();
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes("ASCII"));
            byte[] hash = md.digest();

            for(Byte b : hash){
                sb.append(b);
            }

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}

