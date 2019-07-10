package com.example.chapter3.homework;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Placeholder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment extends Fragment {

    private LottieAnimationView anim;
    private RecyclerView viewList;
    private AnimatorSet set;
    private MyListAdapter mAdapter;
    private List<String> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        View v = inflater.inflate(R.layout.fragment_placeholder, container, false);
        anim = v.findViewById(R.id.loading);
        viewList = v.findViewById(R.id.viewList);
        viewList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MyListAdapter();
        viewList.setAdapter(mAdapter);
        for(int i = 0 ;i < 20 ;i++){
            data.add(i + "");
        }
        mAdapter.setList(data);
        mAdapter.notifyDataSetChanged();
        viewList.setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(anim, "Alpha", 1.0f, 0.0f);
                animator1.setDuration(1000);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(viewList, "Alpha", 0.0f, 1.0f);
                animator2.setDuration(1000);
                viewList.setVisibility(View.VISIBLE);
                set = new AnimatorSet();
                set.playTogether(animator1, animator2);
                set.start();
            }
        }, 5000);
    }
}
