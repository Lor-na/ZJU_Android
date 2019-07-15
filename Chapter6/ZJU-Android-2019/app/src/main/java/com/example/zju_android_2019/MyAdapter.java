package com.example.zju_android_2019;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.zju_android_2019.room.NoteEntity;
import com.example.zju_android_2019.room.NoteOperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final NoteOperator operator;
    private List<NoteEntity> noteList = new ArrayList<>();

    public MyAdapter(NoteOperator operator){ this.operator = operator; }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MyViewHolder.create(parent.getContext(), parent, operator);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(noteList.get(position));
    }

    public void setList(List<NoteEntity> list) {
        if(list == null) return;

        Collections.sort(list, new Comparator<NoteEntity>() {
            @Override
            public int compare(NoteEntity o1, NoteEntity o2) {

                if(o1.getDone() && !o2.getDone()) return 1;
                if(o2.getDone() && !o1.getDone()) return -1;

                if(o1.getPriority() > o2.getPriority()) return -1;
                else if(o1.getPriority() < o2.getPriority()) return 1;
                else return 0;
            }
        });

        noteList = list;
        notifyDataSetChanged();
    }
}
