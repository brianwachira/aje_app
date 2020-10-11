package com.example.ex_contactapp.volley;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public class VolleyContactGroup {


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int id;

    private String groupname;


    private String numofcontacts;

    private int userId;

    public VolleyContactGroup(Integer id, String groupname, String numofcontacts, int userId) {
        this.id = id;
        this.groupname = groupname;
        this.numofcontacts = numofcontacts;
        this.userId = userId;
    }
}
