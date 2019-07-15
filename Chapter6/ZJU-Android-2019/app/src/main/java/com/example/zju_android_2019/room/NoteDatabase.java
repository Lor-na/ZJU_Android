package com.example.zju_android_2019.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NoteEntity.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    private static final String DatabaseName = "note.db";
    private static NoteDatabase instance;

    public static NoteDatabase getDatabase(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext()
            , NoteDatabase.class, DatabaseName).allowMainThreadQueries().build();
        }
        return instance;
    }

    public static void onDestroy() {
        instance = null;
    }

    public abstract NoteEntityDao getNoteEntityDao();

}
