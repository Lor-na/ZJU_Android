package com.example.zju_android_2019.room;


import android.provider.ContactsContract;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteEntityDao {

    @Query("select * from Note")
    List<NoteEntity> getNoteList();

    @Insert
    void add(NoteEntity noteEntity);

    @Delete
    void delete(NoteEntity noteEntity);

    @Update
    void update(NoteEntity noteEntity);
}
