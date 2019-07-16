package com.my.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewAdapter viewAdapter;
    private LayoutInflater inflater;
    private List<View> pages = new ArrayList<>();
    private Button checkBtn;

    private String[] mPermissionsArrays = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private final static int REQUEST_PERMISSION = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        checkBtn = findViewById(R.id.permissionCheck);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissionAllGranted(mPermissionsArrays)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(mPermissionsArrays, REQUEST_PERMISSION);
                    }
                } else {
                    Toast.makeText(ImageActivity.this, "已经获取所有所需权限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewPager = findViewById(R.id.imgViewPager);
        inflater = getLayoutInflater();

        addImages();

        viewAdapter = new ViewAdapter();
        viewAdapter.setDatas(pages);
        viewPager.setAdapter(viewAdapter);

    }

    private void refresh(){
        pages.clear();
        addImages();
        viewAdapter.setDatas(pages);
        viewPager.setAdapter(viewAdapter);
    }

    private void addImages(){
        addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562328963756&di=9c0c6c839381c8314a3ce8e7db61deb2&imgtype=0&src=http%3A%2F%2Fpic13.nipic.com%2F20110316%2F5961966_124313527122_2.jpg");
        addImage("/sdcard/DCIM/Camera/IMG_20190225_095617.jpg", true);
        addImage(R.drawable.icon_volume);
    }

    private void addImage(int resId) {
        ImageView imageView = (ImageView) inflater.inflate(R.layout.page_image, null);
        Glide.with(this)
                .load(resId)
                .error(R.drawable.icon_failure)
                .into(imageView);
        pages.add(imageView);
    }

    private void addImage(String path, Boolean fromSD) {
        if(fromSD && !checkPermissionAllGranted(mPermissionsArrays)) return;
        addImage(path);
    }

    private void addImage(String path) {
        ImageView imageView = (ImageView) inflater.inflate(R.layout.page_image, null);
        Glide.with(this)
                .load(path)
                .error(R.drawable.icon_failure)
                .into(imageView);
        pages.add(imageView);
    }

    private boolean checkPermissionAllGranted(String[] permissions) {
        // 6.0以下不需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults.length > i && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已经授权" + permissions[i], Toast.LENGTH_LONG).show();
                }
            }
        }
        refresh();
    }
}
