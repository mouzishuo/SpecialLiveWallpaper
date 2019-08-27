package org.video.speciallivewallpaper.bean;

import android.graphics.Bitmap;

public class Video {
    private Bitmap bitmap;
    private String duration;
    private String path;
    private String fileName;

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap paramBitmap) {
        bitmap = paramBitmap;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String paramString) {
        duration = paramString;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String paramString) {
        path = paramString;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }
}
