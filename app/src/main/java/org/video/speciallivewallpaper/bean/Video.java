package org.video.speciallivewallpaper.bean;

import android.graphics.Bitmap;

public class Video {
    private Bitmap bitmap;
    private String duration;
    private String path;
    private String fileName;

    public Video() {
    }

    public Video(String paramString1, Bitmap paramBitmap, String paramString2) {
        this.path = paramString1;
        this.bitmap = paramBitmap;
        this.duration = paramString2;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public String getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }

    public void setBitmap(Bitmap paramBitmap) {
        bitmap = paramBitmap;
    }

    public void setDuration(String paramString) {
        duration = paramString;
    }

    public void setPath(String paramString) {
        path = paramString;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
