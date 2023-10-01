package com.example.notesapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactsDAO {

    @Query(" select * from contacts order by Key_Id DESC")
    List<Contacts> getAllContacts();

    @Query(" DELETE FROM contacts")
    void deleteAllData();

    @Insert()
    long addData(Contacts contacts);

    @Update()
    void updateData(Contacts contacts);

    @Delete()
    void deleteData(Contacts contacts);

}
