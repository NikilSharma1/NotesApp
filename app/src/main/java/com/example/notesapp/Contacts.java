package com.example.notesapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contacts {

    @PrimaryKey(autoGenerate = true)
    private int Key_Id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "email")
    private String email;



    public Contacts(int Key_Id,String name,String email){
        this.name=name;
        this.email=email;
        this.Key_Id=Key_Id;
    }

    @Ignore
    public Contacts(String name, String email){
        this.name=name;
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getKey_Id() {
        return Key_Id;
    }

    public void setKey_Id(int key_Id) {
        Key_Id = key_Id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}
