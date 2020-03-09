package com.example.ex_contactapp.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ex_contactapp.data.Databases.ContactGroupDatabase;
import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Repositories.ContactGroupRepository;

import java.util.List;

public class ContactGroupViewModel extends ViewModel {

    private ContactGroupRepository contactGroupRepository;

    public  ContactGroupViewModel(Context context){
        contactGroupRepository = ContactGroupRepository.getInstance(ContactGroupDatabase.getAppDatabase(context).contactGroupDao());
    }

    public void createGroup(String groupname, String numofcontacts){

        contactGroupRepository.insertContactGroup(groupname,numofcontacts);
    }

    public void deleteGroup(int groupId){
        contactGroupRepository.deleteContactGroup(groupId);
    }


    public List<ContactGroup> readGroup(){

        return contactGroupRepository.ReadContactGroups();
    }

    public static class Factory implements ViewModelProvider.Factory{
        private final Context contxt;

        public Factory(Context contxt){
            this.contxt = contxt.getApplicationContext();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ContactGroupViewModel(contxt);
        }
    }
}
