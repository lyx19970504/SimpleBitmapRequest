package com.example.neglide;

import android.content.Context;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

public class BitmapRequest {

    //图片标识
    private String urlMD5;

    public String getUrlMD5() {
        return urlMD5;
    }

    public void setUrlMD5(String urlMD5) {
        this.urlMD5 = urlMD5;
    }

    private Context context;

    //占位图片
    private int resId;

    //设置图片的ImageView
    private SoftReference<ImageView> imageView;

    //请求监听
    private RequestListener requestListener;

    //请求图片地址
    private String url;

    public BitmapRequest(Context context){
        this.context = context;
    }

    public BitmapRequest load(String url){
        this.url = url;
        this.urlMD5 = MD5Util.toMD5(url);
        return this;
    }

    public BitmapRequest loading(int resId){
        this.resId = resId;
        return this;
    }

    public BitmapRequest listener(RequestListener listener){
        this.requestListener = listener;
        return this;
    }

    public void into(ImageView imageView){
        imageView.setTag(this.urlMD5);
        this.imageView = new SoftReference<>(imageView);
        RequestManager.getInstance().addBitmapRequest(this);
    }

    public int getResId() {
        return resId;
    }

    public ImageView getImageView() {
        return imageView.get();
    }


    public RequestListener getRequestListener() {
        return requestListener;
    }

    public String getUrl() {
        return url;
    }


}
