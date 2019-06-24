package com.example.neglide.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.neglide.Application.MyApplication;
import com.example.neglide.BitmapRequest;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DiskBitmapCache{

    private static DiskBitmapCache sDiskBitmapCache;
    private static DiskLruCache sDiskLruCache;

    public static DiskBitmapCache getInstance(){
        if(sDiskBitmapCache == null){
            sDiskBitmapCache = new DiskBitmapCache();
        }
        return sDiskBitmapCache;
    }

    public DiskBitmapCache(){
        File cacheDir = MyApplication.getContext().getExternalCacheDir();
        long maxCacheSize = 50 * 1024 * 1024;  //最大缓存为50M
        try {
            sDiskLruCache = DiskLruCache.open(cacheDir,1, 1, maxCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(BitmapRequest request,Bitmap bitmap){
        try {
            DiskLruCache.Editor editor = sDiskLruCache.edit(request.getUrlMD5());
            OutputStream outputStream = editor.newOutputStream(0);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            editor.commit();
            sDiskLruCache.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap get(BitmapRequest request){
        try {
            DiskLruCache.Snapshot snapshot = sDiskLruCache.get(request.getUrlMD5());
            if(snapshot != null){
                return BitmapFactory.decodeStream(snapshot.getInputStream(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void remove(BitmapRequest request){
        try {
            sDiskLruCache.remove(request.getUrlMD5());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
