package com.example.ex_contactapp.models;

import java.util.List;

public class ModelContactGroups {

    private String groupId,groupName;
    private List<String> contactIdList;

    public ModelContactGroups(String groupId, String groupName, List<String> contactIdList) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.contactIdList = contactIdList;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getContactIdList() {
        return contactIdList;
    }

    public void setContactIdList(List<String> contactIdList) {
        this.contactIdList = contactIdList;
    }
}
