package com.example.zju_android_2019;

import android.content.Intent;
import android.os.Bundle;

import com.example.zju_android_2019.room.NoteDatabase;
import com.example.zju_android_2019.room.NoteEntity;
import com.example.zju_android_2019.room.NoteOperator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, NoteActivity.class), 1);
            }
        });

        rv = findViewById(R.id.RV);
        rv.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(new NoteOperator() {
            @Override
            public void deleteNote(NoteEntity note) {
                MainActivity.this.deleteNote(note);
            }

            @Override
            public void updateNote(NoteEntity note) {
                MainActivity.this.updateNote(note);
            }
        });
        rv.setAdapter(myAdapter);
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1) {
            NoteEntity noteEntity = new NoteEntity(data.getStringExtra("Content"),
                    data.getStringExtra("Date"),
                    data.getBooleanExtra("Done", false),
                    data.getIntExtra("Priority",1));
            NoteDatabase.getDatabase(getApplicationContext()).getNoteEntityDao().add(noteEntity);
            refresh();
        }
    }

    private void deleteNote(NoteEntity noteEntity){
        NoteDatabase.getDatabase(getApplicationContext()).getNoteEntityDao().delete(noteEntity);
        refresh();
    }

    private void updateNote(NoteEntity noteEntity){
        NoteDatabase.getDatabase(getApplicationContext()).getNoteEntityDao().update(noteEntity);
        refresh();
    }

    private void refresh(){
        List<NoteEntity> noteList;
        noteList = NoteDatabase.getDatabase(getApplicationContext()).getNoteEntityDao().getNoteList();
        myAdapter.setList(noteList);
    }
}
