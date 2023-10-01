package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.ArrayList;

@Database(entities = Contacts.class,exportSchema = false, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {
    public static final String DATABASE_NAME = "contactsdb";
    public static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDataBase(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context,DatabaseHelper.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract ContactsDAO contactsDAO();
}
