package com.example.ex_contactapp.data.Repositories;

import com.example.ex_contactapp.data.DAO.GrouplistDAO;
import com.example.ex_contactapp.data.Entities.Grouplist;

public class GroupListRepository {
    private final GrouplistDAO grouplistDAO;
    private static GroupListRepository instance;

    private GroupListRepository(GrouplistDAO grouplistDAO){
        this.grouplistDAO = grouplistDAO;
    }

    public static GroupListRepository getInstance(GrouplistDAO grouplistDAO){
        if(instance == null){
            instance = new GroupListRepository(grouplistDAO);
        }
        return instance;
    }

    public void insertGroupList(String firstname,String lastname, String middlename,String phonenumber,Integer groupId){
        Grouplist grouplist = new Grouplist(firstname,lastname,middlename,phonenumber,groupId);
        grouplistDAO.insertGroupList(grouplist);

    }
}
