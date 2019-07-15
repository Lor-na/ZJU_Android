package com.example.zju_android_2019.room;

public interface NoteOperator {
    void deleteNote(NoteEntity note);

    void updateNote(NoteEntity note);
}
