package com.example.neglide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.example.neglide.cache.DiskBitmapCache;
import com.example.neglide.cache.DoubleLruCache;
import com.example.neglide.cache.MemoryLruCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestDispatcher extends Thread{

    private Handler handler = new Handler(Looper.getMainLooper());

    private LinkedBlockingQueue<BitmapRequest> linkedBlockingQueue;

    public RequestDispatcher(LinkedBlockingQueue<BitmapRequest> linkedBlockingDeque){
        this.linkedBlockingQueue = linkedBlockingDeque;
    }


    @Override
    public void run() {
        super.run();
        while (!isInterrupted()){
            try {
                //从队列中获取图片请求
                BitmapRequest request = linkedBlockingQueue.take();
                //显示占位图片
                showLoadingImage(request);
                //加载图片
                Bitmap bitmap = findBitmap(request);
                //将图片显示到ImageView
                showImageView(request,bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void showLoadingImage(BitmapRequest request) {
        if(request.getResId() > 0 && request.getImageView() != null){
            final int resId = request.getResId();
            final ImageView imageView = request.getImageView();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(resId);
                }
            });
        }
    }

    private Bitmap findBitmap(BitmapRequest request) {
       Bitmap bitmap;
       bitmap = DoubleLruCache.getInstance().get(request);
       if(bitmap == null){
           bitmap = downloadBitmap(request.getUrl());
           MemoryLruCache.getInstance().put(request,bitmap);
           DiskBitmapCache.getInstance().put(request,bitmap);
       }
       return bitmap;
    }

    public Bitmap downloadBitmap(String uri){
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    private void showImageView(final BitmapRequest request, final Bitmap bitmap) {
        if(request.getImageView() != null && bitmap != null && request.getUrlMD5().equals(request.getImageView().getTag())){
            final ImageView imageView = request.getImageView();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);
                    //设置监听器
                    if(request.getRequestListener() != null){
                        RequestListener listener = request.getRequestListener();
                        listener.onSuccess(bitmap);
                    }
                }
            });
        }
    }
}
