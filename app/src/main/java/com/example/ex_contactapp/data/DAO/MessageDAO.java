package com.example.ex_contactapp.data.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.ex_contactapp.data.Entities.Grouplist;
import com.example.ex_contactapp.data.Entities.Message;

import java.util.List;

@Dao
public interface MessageDAO {


    @Query("SELECT * FROM message ORDER BY `messageid` DESC")
    LiveData<List<Message>> getMessages();

    @Query("SELECT * FROM message ORDER BY 'messageid' DESC")
    List<Message> getMessagesForSync();

    @Transaction
    @Query("SELECT * FROM message WHERE message.messageid=:id")
    LiveData<Message> getMessagesById(int id);

    @Query("UPDATE message SET remotegroupid=:remoteid WHERE message.groupid=:groupid")
    void updateMessage(int remoteid,int groupid);

    @Insert
    void insertMessage(Message message);

    @Query("DELETE FROM message WHERE message.messageid=:id")
    void deleteMessage(int id);

    @Query("SELECT messageContent FROM message WHERE message.messageContent=:messageContent AND message.groupid=:groupid")
    String showMessageByRemoteId(String messageContent,int groupid);
}
