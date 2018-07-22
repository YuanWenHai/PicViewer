package com.will.picviewer.file;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    public static void saveBytesToDisk(String filePath ,byte[] bytes){
        BufferedOutputStream output = null;
        File path = new File(filePath.substring(0,filePath.lastIndexOf(File.separator)));
        if(!path.exists()){
            path.mkdirs();
        }
        try{
            output = new BufferedOutputStream(new FileOutputStream(filePath));
            output.write(bytes);
        }catch (IOException i){
            i.printStackTrace();
            Log.e("FileHelper","io exception");
        }finally {
            if(output!=null){
                try{
                    output.close();
                }catch (IOException i){
                    i.printStackTrace();
                }
            }

        }
    }
    public static boolean saveBitmapToDisk(String filePath, Bitmap bitmap){
        BufferedOutputStream output = null;
        File path = new File(filePath.substring(0,filePath.lastIndexOf(File.separator)));
        if(!path.exists()){
            path.mkdirs();
        }
        try{
            output = new BufferedOutputStream(new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.PNG,100,output);
        }catch (IOException i){
            i.printStackTrace();
            Log.e("FileHelper","io exception");
            return false;
        }finally {
            if(output!=null){
                try{
                    output.close();
                }catch (IOException i){
                    i.printStackTrace();
                }
            }

        }
        return true;
    }
}
