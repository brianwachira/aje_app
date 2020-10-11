package com.example.ex_contactapp.data.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Entities.Message;

import java.util.List;

public class ContactGroupAndMessage {

    public ContactGroupAndMessage(){

    }

    @Embedded
    public ContactGroup contactGroup;

    @Relation(parentColumn = "id",entityColumn = "groupid", entity = Message.class)
    private List <Message> messages;

    public ContactGroup getContactGroup() {
        return contactGroup;
    }

    public void setContactGroup(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }


}
