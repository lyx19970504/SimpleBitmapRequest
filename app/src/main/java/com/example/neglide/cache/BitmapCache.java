package com.example.neglide.cache;

import android.graphics.Bitmap;

import com.example.neglide.BitmapRequest;

public interface BitmapCache {

    void put(BitmapRequest request, Bitmap bitmap);

    Bitmap get(BitmapRequest request);

    void remove(BitmapRequest request);
}
