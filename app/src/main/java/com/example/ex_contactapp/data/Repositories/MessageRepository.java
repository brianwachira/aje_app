package com.example.ex_contactapp.data.Repositories;

import com.example.ex_contactapp.data.DAO.MessageDAO;
import com.example.ex_contactapp.data.Entities.Message;
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

    public List<String> getMessagesById(int id) {
        return messageDAO.getMessagesById(id);
    }
}
