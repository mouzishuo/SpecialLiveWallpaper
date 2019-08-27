package org.video.speciallivewallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

public class VideoWallpaperService extends WallpaperService {
    public static final String INTENT_FILTER = "org.video.speciallivewallpaper";
    public static final String KEY_ACTION = "action";
    public static final int NO_SOUND = 100;
    public static final int YES_SOUND = 101;
    private static final String TAG = "TAG";
    private MyWallpaperEngine mWallpaperEngine;

    public static void setMute(Context context, boolean mute){
        Intent intent = new Intent(INTENT_FILTER);
        intent.putExtra(KEY_ACTION, mute ? NO_SOUND : YES_SOUND);
        context.sendBroadcast(intent);
    }

    @Override
    public Engine onCreateEngine() {
        Log.i(TAG, "onCreateEngine: ----");
        mWallpaperEngine = new MyWallpaperEngine();
        return mWallpaperEngine;
    }

    private class MyWallpaperEngine extends Engine {
        private MediaPlayer mediaPlayer;
        private BroadcastReceiver broadcastReceiver;

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "onSurfaceCreated: ----");
            super.onSurfaceCreated(holder);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setSurface(holder.getSurface());
            playVideo();
        }

        private void playVideo(){
            SharedPreferences mp4_data = getSharedPreferences("mp4_data", 0);
            String data = mp4_data.getString("path", "a.mp4");
            try {
                mediaPlayer.setDataSource(data);
                mediaPlayer.setLooping(true);
                int soundType = getSharedPreferences("soundType", 0).getInt("type", 100);
                if (soundType == 100) {
                    mediaPlayer.setVolume(0, 0);
                } else if (soundType == 101) {
                    mediaPlayer.setVolume(1.0f, 1.0f);
                }
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void updateVideo() {
            mediaPlayer.reset();
            playVideo();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                mediaPlayer.start();
            } else {
                mediaPlayer.pause();
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.i(TAG, "onSurfaceDestroyed: ----");
            super.onSurfaceDestroyed(holder);
            mediaPlayer.release();
            mediaPlayer = null;
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            Log.i(TAG, "onCreate: ----");
            super.onCreate(surfaceHolder);

            IntentFilter intentFilter = new IntentFilter(INTENT_FILTER);
            intentFilter.addAction(Intent.ACTION_WALLPAPER_CHANGED);
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(Intent.ACTION_WALLPAPER_CHANGED)) {
                        updateVideo();
                        return;
                    }
                    int intExtra = intent.getIntExtra(KEY_ACTION, NO_SOUND);
                    getSharedPreferences("soundType", 0).edit().putInt("type", intExtra).commit();
                    switch (intExtra) {
                        case NO_SOUND:
                            mediaPlayer.setVolume(0, 0);
                            break;
                        case YES_SOUND:
                            mediaPlayer.setVolume(1.0f, 1.0f);
                            break;
                        default:
                            break;
                    }
                }
            };
            registerReceiver(broadcastReceiver, intentFilter);
        }

        @Override
        public void onDestroy() {
            Log.i(TAG, "onDestroy: ----");
            if (broadcastReceiver != null) {
                unregisterReceiver(broadcastReceiver);
            }
            super.onDestroy();
        }
    }
}
