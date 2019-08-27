package org.video.speciallivewallpaper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import org.video.speciallivewallpaper.bean.Video;

import java.util.ArrayList;

public class VideoUtil {
    public static ArrayList<Video> initData(Context paramContext) {
        DurationUtils localDurationUtils = new DurationUtils();
        ArrayList localArrayList = new ArrayList();
        Cursor localCursor = paramContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_data", "_id", "title", "mime_type", "duration", "_size"}, null, null, null);
        if (localCursor.moveToFirst()) {
            do {
                long duration = localCursor.getLong(localCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                if (duration > 10000L) {
                    Video localVideo = new Video();
                    String path = localCursor.getString(localCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    localVideo.setPath(path);

                    int index = path.lastIndexOf('/');
                    String fileName = path.substring(index + 1);
                    localVideo.setFileName(fileName);

                    int id = localCursor.getInt(localCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    localVideo.setDuration(localDurationUtils.stringForTime((int) duration));

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inDither = false;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    localVideo.setBitmap(MediaStore.Video.Thumbnails.getThumbnail(paramContext.getContentResolver(), id, MediaStore.Video.Thumbnails.MINI_KIND, options));

                    localArrayList.add(localVideo);
                }
            } while (localCursor.moveToNext());
        }
        return localArrayList;
    }
}
