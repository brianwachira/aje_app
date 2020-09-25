package com.example.ex_contactapp.viewmodels;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.ex_contactapp.data.Databases.ContactGroupDatabase;
import com.example.ex_contactapp.data.Repositories.GroupListRepository;

public class GroupListViewModel extends ViewModel {
    private GroupListRepository groupListRepository;

    public GroupListViewModel(Context context) {
        this.groupListRepository = GroupListRepository.getInstance(ContactGroupDatabase.getAppDatabase(context).grouplistDAO());
    }

    public void createGroupList(String firstname, String lastname, String middlename, String phonenumber, Integer groupId) {
        this.groupListRepository.insertGroupList(firstname, lastname, middlename, phonenumber, groupId);
    }

    public void deleteContactFromGroup(int id) {
        this.groupListRepository.deleteContact(id);
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
