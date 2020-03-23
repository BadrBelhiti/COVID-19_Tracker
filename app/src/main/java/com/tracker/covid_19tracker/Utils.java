package com.tracker.covid_19tracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Utils {

    public static byte[] getBytes(File file){
        FileInputStream fileInputStream;
        byte[] bytes = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        return bytes;
    }

    public static byte[] getBytes(FileInputStream fileInputStream, int size){
        byte[] bytes = new byte[size];
        try {
            fileInputStream.read(bytes);
            fileInputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return bytes;
    }

}
