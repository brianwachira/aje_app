package com.example.ex_contactapp.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ex_contactapp.data.Databases.ContactGroupDatabase;
import com.example.ex_contactapp.data.Repositories.GroupListRepository;

public class GroupListViewModel extends ViewModel {

    private GroupListRepository groupListRepository;

    public GroupListViewModel(Context context){
        groupListRepository = GroupListRepository.getInstance(ContactGroupDatabase.getAppDatabase(context).grouplistDAO());
    }

    public void createGroupList(String firstname,String lastname,String middlename,String phonenumber,Integer groupId){

        groupListRepository.insertGroupList(firstname,lastname,middlename,phonenumber,groupId);
    }

    public static class Factory implements ViewModelProvider.Factory{

        private final Context contxt;

        public Factory(Context contxt) {
            this.contxt = contxt.getApplicationContext();
        }


        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return null;
        }
    }
}
