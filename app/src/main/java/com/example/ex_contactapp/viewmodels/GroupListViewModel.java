package com.example.ex_contactapp.viewmodels;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.ex_contactapp.data.Databases.ContactGroupDatabase;
import com.example.ex_contactapp.data.Entities.Grouplist;
import com.example.ex_contactapp.data.Repositories.GroupListRepository;

import java.util.List;

public class GroupListViewModel extends ViewModel {
    private GroupListRepository groupListRepository;

    public GroupListViewModel(Context context) {
        this.groupListRepository = GroupListRepository.getInstance(ContactGroupDatabase.getAppDatabase(context).grouplistDAO());
    }

    public void createGroupList(String firstname, String lastname, String middlename, String phonenumber, Integer groupId, Integer remotegroupId) {
        this.groupListRepository.insertGroupList(firstname, lastname, middlename, phonenumber, groupId,remotegroupId);
    }

    public void deleteContactFromGroup(int id) {
        this.groupListRepository.deleteContact(id);
    }

    public void updateGrouplistRemoteid(int remoteid, int groupId){
        this.groupListRepository.updateGroupListRemoteID(remoteid,groupId);
    }

    public List<Grouplist> getGroupListForSync(){
        return this.groupListRepository.readGroupListForSync();
    }

    public List<Grouplist>readGroupListById(int id){

        return this.groupListRepository.readGroupListById(id);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context contxt;

        public Factory(Context contxt2) {
            this.contxt = contxt2.getApplicationContext();
        }

        public <T extends ViewModel> T create(Class<T> cls) {
            return (T) new GroupListViewModel(this.contxt);
        }
    }
}
