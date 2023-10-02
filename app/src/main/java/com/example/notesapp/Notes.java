package com.example.notesapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    private long Key_Id;
//    @ColumnInfo(name = "name")
//    private String name;
    @ColumnInfo(name = "note")
    private String note;



    public Notes(long Key_Id,String note){
        this.note=note;
        this.Key_Id=Key_Id;
    }

    @Ignore
    public Notes(String note){
        this.note=note;
    }

    public String getNote() {
        return note;
    }

    public long getKey_Id() {
        return Key_Id;
    }

    public void setKey_Id(int key_Id) {
        Key_Id = key_Id;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
