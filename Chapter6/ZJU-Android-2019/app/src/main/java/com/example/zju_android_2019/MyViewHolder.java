package com.example.zju_android_2019;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zju_android_2019.room.NoteEntity;
import com.example.zju_android_2019.room.NoteOperator;


public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView content, dateView;
    private CheckBox checkBox;
    private ImageButton delete;
    private final NoteOperator operator;


    public MyViewHolder(@NonNull View itemView, NoteOperator operator) {
        super(itemView);
        content = itemView.findViewById(R.id.content_text);
        checkBox = itemView.findViewById(R.id.check_box);
        delete = itemView.findViewById(R.id.delete_button);
        dateView = itemView.findViewById(R.id.date_text);
        this.operator = operator;
    }

    public static MyViewHolder create(Context context, ViewGroup root, NoteOperator operator){
        View v = LayoutInflater.from(context).inflate(R.layout.rv_item, root, false);
        return new MyViewHolder(v, operator);
    }

    public void bind(final NoteEntity noteEntity){

        if(noteEntity == null) return;

        content.setText(noteEntity.getContent());

        checkBox.setOnCheckedChangeListener(null);
        if(noteEntity.getDone()) {
            checkBox.setChecked(true);
            content.setPaintFlags(content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            checkBox.setChecked(false);
            content.setPaintFlags(content.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        dateView.setText(noteEntity.getDate());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                noteEntity.setDone(b);
                operator.updateNote(noteEntity);
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                operator.deleteNote(noteEntity);
            }
        });

        switch (noteEntity.getPriority())
        {
            case 3 :
                itemView.setBackgroundColor(Color.RED);
                break;
            case 2 :
                itemView.setBackgroundColor(Color.GREEN);
                break;
            default :
                itemView.setBackgroundColor(Color.WHITE);
        }
        if(noteEntity.getDone()) itemView.setBackgroundColor(Color.WHITE);
    }
}
