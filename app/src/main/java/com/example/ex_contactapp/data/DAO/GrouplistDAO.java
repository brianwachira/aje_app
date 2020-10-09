package com.example.ex_contactapp.data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Entities.Grouplist;

import java.util.List;

@Dao
public interface GrouplistDAO {

    @Query("SELECT * FROM grouplist ORDER BY 'id' DESC")
    List<Grouplist> getGroupListForSync();

    @Query("SELECT * FROM grouplist WHERE grouplist.groupid=:id")
    List<Grouplist> getGroupContactsById(int id);

    @Insert
    void insertGroupList(Grouplist grouplist);

    @Update
    void updateGroupList(Grouplist grouplist);

    @Query("DELETE FROM grouplist WHERE grouplist.contactid=:id")
    void deleteGroupContact(int id);

    @Query("DELETE  FROM grouplist WHERE grouplist.groupid=:id")
    void deleteAllGroupContacts(int id);
}
