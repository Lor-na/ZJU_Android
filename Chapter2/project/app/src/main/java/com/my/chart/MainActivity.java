package com.my.chart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView chart;
    private MyListAdapter mAdapter;
    private TextView chartTitle;
    private Button changeBtn, insertBtn, deleteBtn;
    private List<ItemData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        chartTitle = findViewById(R.id.chartTitle);
        //chartTitle.setText("更新于");
        updateTime();
        chart = findViewById(R.id.chart);
        chart.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyListAdapter();
        chart.setAdapter(mAdapter);
        for(int i = 0; i < 20;i++){
            ItemData data = new ItemData(i + "", i + 1, (20 - i) * 10);
            //Log.i("Start", data.title + "");
            list.add(data);
        }
        mAdapter.setList(list);
        mAdapter.notifyDataSetChanged();
        changeBtn = findViewById(R.id.Change);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Start", "fromhereGo");
                Intent intent = new Intent(MainActivity.this, ChangeActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        insertBtn = findViewById(R.id.insert);
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        deleteBtn = findViewById(R.id.delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            int No = data.getIntExtra("No", 21);
            String title = data.getStringExtra("title");
            int hot = data.getIntExtra("hot", 10);
            ItemData d = new ItemData(title, No, hot);
            list.set(No-1, d);
            SortList();
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
            updateTime();
        }
        else if(requestCode == 1 && resultCode == 2){
            int No = list.size();
            String title = data.getStringExtra("title");
            int hot = data.getIntExtra("hot", 10);
            ItemData d = new ItemData(title, No, hot);
            list.add(d);
            SortList();
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
            updateTime();
        }
        else if(requestCode == 1 && resultCode == 3){
            int No = data.getIntExtra("No", 21);
            list.remove(No - 1);
            SortList();
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
            updateTime();
        }
    }

    private void updateTime(){
        SimpleDateFormat s = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        chartTitle.setText("更新于" + s.format(date));
    }

    private void SortList(){
        int size = list.size();
        ItemData temp;
        for(int i = 0; i < size; i++){
            for(int j = 0;j < size - 1 - i;j++){
                if(list.get(j).hot < list.get(j + 1).hot){
                    temp = list.get(j);
                    list.set(j,list.get(j+1));
                    list.set(j+1, temp);
                }
            }
        }
        for(int i = 0; i < size; i++){
            temp = list.get(i);
            temp.No = i + 1;
            list.set(i, temp);
        }
    }
}
