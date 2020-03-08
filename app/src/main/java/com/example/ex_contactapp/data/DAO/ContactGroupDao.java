package com.example.ex_contactapp.data.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Relations.ContactGroupAndGroupList;
import com.example.ex_contactapp.data.Relations.ContactGroupAndMessage;

import java.util.List;

@Dao
public interface ContactGroupDao {

    @Query("SELECT * FROM contactgroup ORDER BY `id` DESC")
    List<ContactGroup> getContactGroups();

    @Transaction
    @Query("SELECT * FROM contactgroup")
    List<ContactGroupAndGroupList> getContactGroupAndContacts();

    @Transaction
    @Query("SELECT * FROM contactgroup")
    List<ContactGroupAndMessage> getContactGroupAndMessage();


    @Insert
    void insertGroup(ContactGroup contactGroup);

    @Update
    void updateContactGroup(ContactGroup contactGroup);

    @Query("DELETE FROM contactgroup WHERE contactgroup.id=:id")
    void deleteContactGroup(int id);
}
