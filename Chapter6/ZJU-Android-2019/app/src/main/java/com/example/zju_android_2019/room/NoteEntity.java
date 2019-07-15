package com.example.zju_android_2019.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note")
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int _id;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "done")
    private boolean done;

    @ColumnInfo(name = "Priority")
    private int priority;

    public int get_id(){ return _id; }

    public void set_id(int _id) { this._id = _id; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public boolean getDone() { return done; }

    public void setDone(boolean done) { this.done = done; }

    public int getPriority() { return priority; }

    public void setPriority(int priority) { this.priority = priority; }

    public NoteEntity(String content, String date, boolean done, int priority){
        this.content = content;
        this.date = date;
        this.done = done;
        this.priority = priority;
    }

}
