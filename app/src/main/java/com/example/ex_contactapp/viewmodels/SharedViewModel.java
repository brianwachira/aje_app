package com.example.ex_contactapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private  final MutableLiveData<String> groupName = new MutableLiveData<>();

    private final MutableLiveData<List<String>> currentSelectedContacts = new MutableLiveData<>();

    public void setGroupName(String groupNames){
       groupName.setValue(groupNames);
    }

    public LiveData<String> getGroupName() {
        return groupName;
    }

    public void setCurrentSelectedContacts(List<String> contactIdList){
        currentSelectedContacts.setValue(contactIdList);
    }
    public LiveData<List<String>> getCurrentSelectedContacts() {
        return currentSelectedContacts;
    }

}
