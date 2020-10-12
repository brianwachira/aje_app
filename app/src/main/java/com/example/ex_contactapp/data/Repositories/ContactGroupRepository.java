package com.example.ex_contactapp.data.Repositories;

import androidx.lifecycle.LiveData;

import com.example.ex_contactapp.data.DAO.ContactGroupDao;
import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Relations.ContactGroupAndGroupList;

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

    public List<ContactGroup> ReadContactGroupsForSync(){

        return contactGroupDao.getContactGroupsForSync();
    }

    public Integer readGroupId( String name){
        return  contactGroupDao.getGroupId(name);
    }

    public void deleteContactGroup(int id){
        contactGroupDao.deleteContactGroup(id);
    }

    public LiveData<ContactGroupAndGroupList> readContactGroupAndContactsById(int id){
        return contactGroupDao.getContactGroupAndContactsById(id);
    }
    public String getContactGroupByName(String name){
        return  contactGroupDao.getContactGroupByName(name);
    }

}
