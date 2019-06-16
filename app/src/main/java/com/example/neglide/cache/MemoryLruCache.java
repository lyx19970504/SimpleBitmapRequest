package com.example.neglide.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.example.neglide.BitmapRequest;

public class MemoryLruCache implements BitmapCache{

    private LruCache<String,Bitmap> lruCache;

    private static volatile MemoryLruCache memoryLruCache;

    public static final byte[]lock = new byte[0];

    public static MemoryLruCache getInstance(){
        if(memoryLruCache == null){
            synchronized (lock){
                if(memoryLruCache == null){
                    memoryLruCache = new MemoryLruCache();
                }
            }
        }
        return memoryLruCache;
    }

    public MemoryLruCache(){

        int maxMemorySize = (int) (Runtime.getRuntime().maxMemory() / 16);
        if(maxMemorySize < 0){
            maxMemorySize = 10*1024*1024;   //最大缓存大小:10M
        }
        lruCache = new LruCache<String, Bitmap>(maxMemorySize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //一张图片的大小
                return value.getRowBytes()*value.getHeight();
            }
        };
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        if(bitmap != null){
            lruCache.put(request.getUrlMD5(),bitmap);
        }
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        Bitmap bitmap = lruCache.get(request.getUrlMD5());
        if(bitmap != null){
            return  bitmap;
        }
        return null;
    }

    @Override
    public void remove(BitmapRequest request) {
        Bitmap bitmap = lruCache.get(request.getUrlMD5());
        if(bitmap != null){
            lruCache.remove(request.getUrlMD5());
        }
    }
}
