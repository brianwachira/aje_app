package com.example.ex_contactapp.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.adapters.DialogContactsRvAdapter;
import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Entities.Grouplist;
import com.example.ex_contactapp.data.Relations.ContactGroupAndGroupList;
import com.example.ex_contactapp.models.ModelDialogContacts;
import com.example.ex_contactapp.viewmodels.ContactGroupViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentDialogContacts extends DialogFragment implements DialogContactsRvAdapter.DialogCheckedStatusListener {

    private static String dialogTitle;

    private static Integer groupId;
    private View v;

    private RecyclerView recyclerView;

    private static List<Grouplist> currentSelectedContacts = new ArrayList<>();

    DialogContactsRvAdapter adapter;


     ContactGroup selectedContactGroup;
     LiveData<List<ContactGroupAndGroupList>> contactGroupAndGroupList;
     ContactGroupViewModel contactGroupViewModel;





    public FragmentDialogContacts(){

    }

    public static FragmentDialogContacts newInstance(int id){

        FragmentDialogContacts fragmentDialogContacts = new FragmentDialogContacts();
        Bundle args = new Bundle();

        groupId = id;
        args.putString("title",dialogTitle);
        fragmentDialogContacts.setArguments(args);




        return fragmentDialogContacts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.dialog_contacts, container,false);
        recyclerView = v.findViewById(R.id.rv_dialog_contacts);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        RecyclerView.LayoutManager layoutManager = linearLayoutManager;

        recyclerView.setLayoutManager(layoutManager);

        contactGroupViewModel = ViewModelProviders.of(this,new ContactGroupViewModel.Factory(getActivity().getApplicationContext())).get(ContactGroupViewModel.class);

        //contactGroupAndGroupList = contactGroupViewModel.readGroupAndContacts(groupId);

        contactGroupViewModel.readGroupAndContacts(groupId).observe(this,contactGroupAndGroupLists ->


                );
        //selectedContactGroup = contactGroupAndGroupList.observe(this,);




        adapter = new DialogContactsRvAdapter(getContext(), getSelectedContacts(), this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return v;
    }
    public void setContactDetailsNeeded(ContactGroup contactGroup, List<Grouplist> grouplist){
        selectedContactGroup = contactGroup;
        currentSelectedContacts = grouplist;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get the field from view

    }

    private List<ModelDialogContacts> getSelectedContacts(){
        List <ModelDialogContacts> list = new ArrayList<>();

        try {
            for (String contactid:currentSelectedContacts) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Cursor cursor = getContext().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{String.valueOf(contactid)},
                            ContactsContract.Contacts.DISPLAY_NAME + " ASC");
                    cursor.moveToFirst();

                    while (cursor.moveToNext()){
                        Toast.makeText(getContext(),cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),Toast.LENGTH_LONG).show();
                        list.add(new ModelDialogContacts(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)),
                                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
                    }
                    cursor.close();
                }

            }
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return list;
    }

    @Override
    public void onItemChecked(String contactId) {

    }

    @Override
    public void onItemUnchecked(String contactId) {

    }
}
