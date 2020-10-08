package com.example.ex_contactapp.data.Repositories;

import androidx.lifecycle.LiveData;

import com.example.ex_contactapp.data.DAO.MessageDAO;
import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Entities.Message;
import com.example.ex_contactapp.data.Relations.ContactGroupAndGroupList;

import java.util.List;

public class MessageRepository {
    private static MessageRepository instance;
    private final MessageDAO messageDAO;

    public MessageRepository(MessageDAO messageDAO2) {
        this.messageDAO = messageDAO2;
    }

    public static MessageRepository getInstance(MessageDAO messageDAO2) {
        if (instance == null) {
            instance = new MessageRepository(messageDAO2);
        }
        return instance;
    }

    public void insertMessage(String messageContent, int groupId) {
        messageDAO.insertMessage(new Message(messageContent, groupId));
    }

    public void deleteMessage(int messageId){
        messageDAO.deleteMessage(messageId);
    }

    public LiveData<List<Message>> ReadMessages(){

        return messageDAO.getMessages();
    }

    public LiveData<Message> getMessagesById(int id) {
        return messageDAO.getMessagesById(id);
    }

}
