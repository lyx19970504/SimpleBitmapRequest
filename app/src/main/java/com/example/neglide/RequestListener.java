package com.example.neglide;

import android.graphics.Bitmap;

public interface RequestListener {

    void onSuccess(Bitmap bitmap);
    void onFailed();
}
