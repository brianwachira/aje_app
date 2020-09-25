package com.example.ex_contactapp.data.Relations;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Entities.Grouplist;

import java.util.List;

public class ContactGroupAndGroupList {

    public ContactGroupAndGroupList(){

    }

    @Embedded
    public ContactGroup contactGroup;

    @Relation(parentColumn = "id",entityColumn = "groupid", entity = Grouplist.class)
    private List<Grouplist> grouplist;


    public ContactGroup getContactGroup() {
        return contactGroup;
}
    public void setContactGroup(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;

    }

    public List<Grouplist> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(List<Grouplist> grouplist) {
        this.grouplist = grouplist;
    }
    @Ignore
    public ContactGroupAndGroupList(ContactGroup contactGroup, List<Grouplist> grouplist) {
        this.contactGroup = contactGroup;
        this.grouplist = grouplist;
    }
}
