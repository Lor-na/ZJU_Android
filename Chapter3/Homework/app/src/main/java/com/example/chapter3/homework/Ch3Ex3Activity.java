package com.example.chapter3.homework;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 ViewPager 和 Fragment 做一个简单版的好友列表界面
 * 1. 使用 ViewPager 和 Fragment 做个可滑动界面
 * 2. 使用 TabLayout 添加 Tab 支持
 * 3. 对于好友列表 Fragment，使用 Lottie 实现 Loading 效果，在 5s 后展示实际的列表，要求这里的动效是淡入淡出
 */
public class Ch3Ex3Activity extends AppCompatActivity {

    private ViewPager pager;
    private TabLayout tab;
    private List<Fragment> fragList;
    private List<String> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch3ex3);
        fragList = new ArrayList<>();
        fragList.add(new PlaceholderFragment());
        fragList.add(new HelloFragment());

        titleList = new ArrayList<>();
        titleList.add("PlaceHolder");
        titleList.add("Hello");

        // TODO: ex3-1. 添加 ViewPager 和 Fragment 做可滑动界面
        pager = findViewById(R.id.viewPager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragList.get(i);
            }

            @Override
            public int getCount() {
                return fragList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }
        });

        // TODO: ex3-2, 添加 TabLayout 支持 Tab
        tab = findViewById(R.id.tab);
        tab.setupWithViewPager(pager);
    }
}
