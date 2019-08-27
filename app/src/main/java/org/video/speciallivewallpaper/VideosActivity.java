package org.video.speciallivewallpaper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.video.speciallivewallpaper.bean.Video;

import java.util.ArrayList;

public class VideosActivity extends Activity {

    public SharedPreferences sp;
    public SharedPreferences.Editor edit;
    private RecyclerView rv;
    private ArrayList<Video> videos;
    private final int REQUEST_STORAGE = 0x13;
    private final int REQUEST_CODE = 0x12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        checkPermission();
    }

    private void initView() {
        initData();
        rv = findViewById(R.id.rv);
        rv.setItemAnimator(new DefaultItemAnimator());
        VideosRecyclerViewAdapter videosRecyclerViewAdapter = new VideosRecyclerViewAdapter(this, this.videos);
        rv.setAdapter(videosRecyclerViewAdapter);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        videosRecyclerViewAdapter.myOnClickItemListener(new MyOnClickItem() {
            @Override
            public void onClickItem(int position) {
                if (save(VideosActivity.this.videos.get(position).getPath())) {
                    setToWallPaper();
                } else {
                    Toast.makeText(VideosActivity.this, "Something wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean save(String paramString) {
        edit.remove("path");
        edit.putString("path", paramString);
        return edit.commit();
    }

    @SuppressLint("NewApi")
    public void setToWallPaper() {
        Intent localIntent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        localIntent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, VideoWallpaperService.class));
        startActivityForResult(localIntent, REQUEST_CODE);
    }

    private void initData() {
        sp = getSharedPreferences("mp4_data", 0);
        edit = sp.edit();
        this.videos = new ArrayList();
        this.videos.addAll(VideoUtil.initData(this));
    }

    public boolean isLiveWallpaperRunning() {
        Object localObject = WallpaperManager.getInstance(this).getWallpaperInfo();
        if (localObject != null) {
            String str = ((WallpaperInfo) localObject).getPackageName();
            return (str.equals(getPackageName()));
        }
        return false;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        } else {
            initView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initView();
                } else {
                    finish();
                }
                return;
            }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE) && isLiveWallpaperRunning() && resultCode == RESULT_OK) {
            Toast.makeText(this, "Set successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
