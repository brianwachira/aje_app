package com.example.ex_contactapp.data.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

import static androidx.room.RoomMasterTable.TABLE_NAME;

@Entity (tableName = TABLE_NAME)
public class ContactGroup {

    public static final String TABLE_NAME = "contactgroup";

    @PrimaryKey(autoGenerate = true) @NonNull
    private Integer groupid;

    @NonNull
    private String groupname;

    @NonNull
    private String numofcontacts;


    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getNumofcontacts() {
        return numofcontacts;
    }

    public void setNumofcontacts(String numofcontacts) {
        this.numofcontacts = numofcontacts;
    }

    @Ignore
    private List<Grouplist> grouplist = null;

public ContactGroup(){

}
/**
 * @param grouplist
 * @param groupname
 * @param numofcontacts
 */
@Ignore
public ContactGroup(String groupname,String numofcontacts,List<Grouplist> grouplist) {
    super();
    this.groupname = groupname;
    this.numofcontacts = numofcontacts;
    this.grouplist = grouplist;

}

}