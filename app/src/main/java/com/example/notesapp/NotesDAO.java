package com.example.notesapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotesDAO {

    @Query(" select * from notes order by Key_Id DESC")
    List<Notes> getAllNotes();

    @Query(" DELETE FROM notes")
    void deleteAllData();

    @Insert()
    long addData(Notes note);

    @Update()
    void updateData(Notes note);

    @Delete()
    void deleteData(Notes note);

}
