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

    @Query("SELECT * FROM contactgroup ORDER BY 'id' DESC")
    List<ContactGroup> getContactGroupsForSync();

    @Query("SELECT * FROM contactgroup ORDER BY `id` DESC")
    LiveData<List<ContactGroup>> getContactGroups();

    //@Query("SELECT * FROM contactgroup WHERE  contactgroup.groupname=:name")


    @Query("SELECT id FROM contactgroup WHERE contactgroup.groupname=:name")
    Integer  getGroupId(String name);

    @Transaction
    @Query("SELECT * FROM contactgroup WHERE contactgroup.id=:id")
    LiveData<ContactGroupAndGroupList> getContactGroupAndContactsById(int id);

    @Transaction
    @Query("SELECT * FROM contactgroup")
    List<ContactGroupAndMessage> getContactGroupAndMessage();


    @Insert
    void insertGroup(ContactGroup contactGroup);

    @Update
    void updateContactGroup(ContactGroup contactGroup);

    @Query("DELETE FROM contactgroup WHERE contactgroup.id=:id")
    void deleteContactGroup(int id);

    @Query("SELECT groupname FROM contactgroup WHERE contactgroup.groupname=:name")
    String getContactGroupByName(String name);

}
