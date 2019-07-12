package com.bytedance.androidcamp.network.dou.model;

import com.google.gson.annotations.SerializedName;

public class PostVideo {
    @SerializedName("url") private String url;
    @SerializedName("success") private boolean success;

    public String getUrl(){ return url; }
    public  boolean isSuccess() { return success; }
}
