package com.example.neglide.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.neglide.Application.MyApplication;
import com.example.neglide.BitmapRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DiskBitmapCache implements BitmapCache {


    private static volatile DiskBitmapCache diskBitmapCache;
    
    private File cacheFile;
    private File file;


    public static DiskBitmapCache getInstance(){
        if(diskBitmapCache == null){
            diskBitmapCache = new DiskBitmapCache();
        }
        return diskBitmapCache;
    }

    public DiskBitmapCache(){
        file = new File(MyApplication.getContext().getCacheDir(),"my_cache");
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        cacheFile = new File(file,request.getUrlMD5());
        if(!cacheFile.exists()) {
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(cacheFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        Bitmap bitmap = null;
        if(file.exists()) {
            File requestFile = new File(file, request.getUrlMD5());
            if (requestFile.exists()) {
                bitmap = BitmapFactory.decodeFile(requestFile.getAbsolutePath());
            }
        }
        return bitmap;
    }

    @Override
    public void remove(BitmapRequest request) {
        File requestFile = new File(file,request.getUrlMD5());
        if(requestFile.exists()){
            requestFile.delete();
        }
    }
}
