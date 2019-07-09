package com.my.chart;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewHolder extends RecyclerView.ViewHolder {

    private TextView NoText;
    private TextView TitleText;
    private TextView HotText;

    public ListViewHolder(@NonNull View itemView){
        super(itemView);
        TitleText = itemView.findViewById(R.id.title);
        NoText = itemView.findViewById(R.id.No);
        HotText = itemView.findViewById(R.id.hot);
    }

    public static ListViewHolder create(Context context, ViewGroup root){
        View v = LayoutInflater.from(context).inflate(R.layout.layout_list_item, root, false);
        return new ListViewHolder(v);
    }

    public void bind(ItemData data){
        if(null == data) return;
        if(data.No <= 3){
            NoText.setTextColor(0xe6face15);
        }else{
            NoText.setTextColor(0x99ffffff);
        }
        NoText.setText(data.No + "、");
        TitleText.setText(data.title);
        HotText.setText(data.hot + "");
    }
}

