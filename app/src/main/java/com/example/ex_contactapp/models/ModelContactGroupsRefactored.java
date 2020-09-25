package com.example.ex_contactapp.models;

public class ModelContactGroupsRefactored {

    public ModelContactGroupsRefactored(Integer groupId, String groupName, String numofcontacts) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.numofcontacts = numofcontacts;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNumofcontacts() {
        return numofcontacts;
    }

    public void setNumofcontacts(String numofcontacts) {
        this.numofcontacts = numofcontacts;
    }

    private Integer groupId;
    private String groupName;
    private String numofcontacts;

}
