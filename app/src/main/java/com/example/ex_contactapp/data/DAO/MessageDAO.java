package com.example.ex_contactapp.data.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Entities.Message;
import com.example.ex_contactapp.data.Relations.ContactGroupAndGroupList;

import java.util.List;

@Dao
public interface MessageDAO {


    @Query("SELECT * FROM message ORDER BY `messageid` DESC")
    LiveData<List<Message>> getMessages();

    @Transaction
    @Query("SELECT * FROM message WHERE message.messageid=:id")
    LiveData<Message> getMessagesById(int id);

    @Insert
    void insertMessage(Message message);

    @Query("DELETE FROM message WHERE message.messageid=:id")
    void deleteMessage(int id);
}
