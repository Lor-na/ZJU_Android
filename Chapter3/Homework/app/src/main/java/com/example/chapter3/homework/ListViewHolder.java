package com.example.chapter3.homework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListViewHolder extends RecyclerView.ViewHolder {

    private TextView TitleText;

    public ListViewHolder(@NonNull View itemView){
        super(itemView);
        TitleText = itemView.findViewById(R.id.title);
    }

    public static ListViewHolder create(Context context, ViewGroup root){
        View v = LayoutInflater.from(context).inflate(R.layout.list_item, root, false);
        return new ListViewHolder(v);
    }

    public void bind(String data){
        if(null == data) return;
        TitleText.setText(data);
    }
}
