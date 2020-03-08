package com.example.ex_contactapp.data.Repositories;

import androidx.lifecycle.LiveData;

import com.example.ex_contactapp.data.DAO.ContactGroupDao;
import com.example.ex_contactapp.data.Entities.ContactGroup;

import java.util.List;

public class ContactGroupRepository {

    private final ContactGroupDao contactGroupDao;
    private static ContactGroupRepository instance;

    private ContactGroupRepository(ContactGroupDao contactGroupDao){
        this.contactGroupDao = contactGroupDao;
    }

    public static ContactGroupRepository getInstance(ContactGroupDao contactGroupDao){
        if(instance == null){
            instance = new ContactGroupRepository(contactGroupDao);
        }
        return instance;
    }
    public void insertContactGroup(String groupname,String numofcontacts){
        ContactGroup newContactGroup = new ContactGroup(groupname,numofcontacts);
        contactGroupDao.insertGroup(newContactGroup);
    }

    public LiveData<List<ContactGroup>> ReadContactGroups(){

        return contactGroupDao.getContactGroups();
    }

}
