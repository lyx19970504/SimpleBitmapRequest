package com.example.neglide.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.neglide.BitmapRequest;

public class DoubleLruCache implements BitmapCache {


    private static DoubleLruCache doubleLruCache;

    private DiskBitmapCache diskBitmapCache;
    private MemoryLruCache memoryLruCache;

    public static DoubleLruCache getInstance(){
        if(doubleLruCache == null){
            doubleLruCache = new DoubleLruCache();
        }
        return doubleLruCache;
    }

    public DoubleLruCache(){
        memoryLruCache = MemoryLruCache.getInstance();
        diskBitmapCache = DiskBitmapCache.getInstance();
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        memoryLruCache.put(request,bitmap);
        diskBitmapCache.put(request,bitmap);
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        Bitmap bitmap = memoryLruCache.get(request);
        if(bitmap == null){
            bitmap = diskBitmapCache.get(request);
        }
        return bitmap;
    }

    @Override
    public void remove(BitmapRequest request) {
        memoryLruCache.remove(request);
        diskBitmapCache.remove(request);
    }
}
