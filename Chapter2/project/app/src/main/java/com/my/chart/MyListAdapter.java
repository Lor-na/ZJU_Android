package com.my.chart;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private List<ItemData> list = new ArrayList<>();

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ListViewHolder.create(parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Log.d("List", list.get(position).title + "");
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        int size = list.size();
        if(size < 20) return list.size();
        else return 20;
    }

    public void setList(List<ItemData> l) {
        if(l == null) return;
        list = l;
        //Log.i("StartSetList",list.get(2).title + "");
        notifyDataSetChanged();
    }
}
