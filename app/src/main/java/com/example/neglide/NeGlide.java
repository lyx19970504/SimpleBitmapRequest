package com.example.neglide;

import android.content.Context;

public class NeGlide {

    public static BitmapRequest with(Context context){
        return new BitmapRequest(context);
    }
}
