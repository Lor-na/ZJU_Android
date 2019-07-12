package com.bytedance.androidcamp.network.dou.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetVideo {
    @SerializedName("feeds") private List<Video> feeds;
    @SerializedName("success") private boolean success;

    public List<Video> GetListVideo(){
        return feeds;
    }

    public boolean GetSuccess(){
        return success;
    }
}
