package com.jesuraj.java.businesscard;

import android.graphics.Bitmap;

public class ProductData {
    private String path;
    private Bitmap bitmap;

    public ProductData(String path, Bitmap bitmap) {
        this.path = path;
        this.bitmap = bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
