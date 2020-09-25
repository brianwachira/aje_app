package com.example.ex_contactapp.data.DAO;

import com.example.ex_contactapp.data.Entities.Message;

import java.util.List;

public interface MessageDAO {
    List<String> getMessagesById(int i);

    void insertMessage(Message message);
}
