package com.example.ex_contactapp.data.Repositories;

import com.example.ex_contactapp.data.DAO.GrouplistDAO;
import com.example.ex_contactapp.data.Entities.Grouplist;

import java.util.List;

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

    public List<Grouplist> readGroupListForSync(){

        return grouplistDAO.getGroupListForSync();
    }

    public List<Grouplist> readGroupListById(int id){
        return grouplistDAO.getGroupContactsById(id);
    }


    public void insertGroupList(String firstname,String lastname, String middlename,String phonenumber,Integer groupId,Integer remoteid){
        Grouplist grouplist = new Grouplist(firstname,lastname,middlename,phonenumber,groupId,remoteid);
        grouplistDAO.insertGroupList(grouplist);

    }

    public void deleteContact(int id) {
        grouplistDAO.deleteGroupContact(id);
    }


    public  void updateGroupListRemoteID(int remoteid, int groupid){
        grouplistDAO.updateGroupList(remoteid,groupid);
    }
}
