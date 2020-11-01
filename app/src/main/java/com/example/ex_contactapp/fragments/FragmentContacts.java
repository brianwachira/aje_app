package com.example.ex_contactapp.fragments;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.adapters.ContactsRvAdapter;
import com.example.ex_contactapp.models.ModelContacts;
import com.example.ex_contactapp.models.ModelContactsBuffer;
import com.example.ex_contactapp.viewmodels.ContactGroupViewModel;
import com.example.ex_contactapp.viewmodels.GroupListViewModel;
import com.example.ex_contactapp.viewmodels.SharedViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FragmentContacts extends Fragment implements ContactsRvAdapter.CheckedStatusListener{

    private  View v;

    private RecyclerView recyclerView;

    private List<ModelContactsBuffer> currentSelectedContacts = new ArrayList<>();

    ContactsRvAdapter adapter;

    private EditText editTextGroupName;

    private CheckBox recyclerviewCheckbox;

    private Button buttonCreateGroup;

    private SharedViewModel sharedViewModel;

    private ContactGroupViewModel contactGroupViewModel;

    private ModelContactsBuffer modelContactsBuffer;

    private GroupListViewModel groupListViewModel;

    ViewPager viewPager;

    public FragmentContacts() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_contacts,container,false);

        recyclerView = v.findViewById(R.id.rv_contacts);

        buttonCreateGroup = v.findViewById(R.id.button_create_group);

        editTextGroupName = v.findViewById(R.id.group_name);

        recyclerviewCheckbox = v.findViewById(R.id.contact_checkbox);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        RecyclerView.LayoutManager layoutManager= linearLayoutManager;

        viewPager = v.findViewById(R.id.viewpager);


        recyclerView.setLayoutManager(layoutManager);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},1);
        }else {
            adapter = new ContactsRvAdapter(getContext(), getContacts(),this);
            recyclerView.setItemViewCacheSize(getContacts().size());
            recyclerView.setAdapter(adapter);
            //  adapter.notifyDataSetChanged();
        }
        sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        contactGroupViewModel = ViewModelProviders.of(this, new ContactGroupViewModel.Factory(getActivity().getApplicationContext())).get(ContactGroupViewModel.class);
        groupListViewModel = ViewModelProviders.of(this,new GroupListViewModel.Factory(getActivity().getApplicationContext())).get(GroupListViewModel.class);

            buttonCreateGroup.setOnClickListener(v -> {

                Log.i("editTextGroupNamelength", String.valueOf(editTextGroupName.getText().toString().length()));
                if (editTextGroupName.getText().toString().length() > 2) {
                    if(currentSelectedContacts.size() > 1){
                        boolean doesContactExist = contactGroupViewModel.getContactGroupByName(editTextGroupName.getText().toString());
                        if(doesContactExist == true){
                            Toast.makeText(getContext(),"Name already taken",Toast.LENGTH_SHORT).show();
                            }else{
                            contactGroupViewModel.createGroup(editTextGroupName.getText().toString(),String.valueOf(currentSelectedContacts.size()),0);
                            Integer groupId = contactGroupViewModel.readGroupId(editTextGroupName.getText().toString());

                            insertGrouplist(groupId);
                            //clearFields();
                        }
                    }else{
                        new AlertDialog.Builder(this.getActivity())
                                .setIcon(R.drawable.ic_error)
                                .setTitle("Insufficient number of group members")
                                .setMessage("A group should have at least 5 members")
                                .setNeutralButton("Ok",null)
                                .show();
                    }
                }else{
                    new AlertDialog.Builder(this.getActivity())
                            .setIcon(R.drawable.ic_error)
                            .setTitle("Group name is too short")
                            .setMessage("A group should have atleast be 3 characters long")
                            .setNeutralButton("Ok",null)
                            .show();

                }
            });


        return v;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //now permission is granted call function again

                 adapter = new ContactsRvAdapter(getContext(), getContacts(),this);

                //List<ModelContacts> list = new ArrayList<>();
                //list = getContacts().size();
                recyclerView.setItemViewCacheSize(getContacts().size());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }
    }

    private List<ModelContacts> getContacts(){

        List<ModelContacts> list = new ArrayList<>();

        try{
            String[] PROJECTION = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Photo.CONTACT_ID };

            String selectionFields =  ""+ ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0 and " + ContactsContract.CommonDataKinds.Phone.TYPE +"=" + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
            String[] selectionArgs = new String[]{"com.google"};

            Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PROJECTION,selectionFields,null,ContactsContract.Contacts.DISPLAY_NAME + " ASC");
            cursor.moveToFirst();

            while(cursor.moveToNext()){

                list.add(new ModelContacts(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)),
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
            }



        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    @Override
    public void onItemChecked(String name, String phonenumber) {
        String firstName = " ";
        String lastName = " ";

        if(name.split("\\w+").length>1){
            lastName = name.substring(name.lastIndexOf(" ")+1);
            firstName = name.substring(0,name.lastIndexOf(' '));
        }else{
            firstName = name;
            lastName = "null";
        }

        modelContactsBuffer = new ModelContactsBuffer(firstName,lastName,phonenumber);
        currentSelectedContacts.add(modelContactsBuffer);
        //currentSelectedContacts.add(contactId);
        //Toast.makeText(getContext(),currentSelectedContacts.toString(),Toast.LENGTH_SHORT).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemUnchecked(String name, String phonenumber) {
        String firstName = " ";
        String lastName = " ";

        if(name.split("\\w+").length>1){
            lastName = name.substring(name.lastIndexOf(" ")+1);
            firstName = name.substring(0,name.lastIndexOf(' '));
        }else{
            firstName = name;
            lastName =  "null";
        }

        //modelContactsBuffer = new ModelContactsBuffer(firstName,lastName,phonenumber);
            currentSelectedContacts.removeIf( currentSelectedContact -> currentSelectedContact.getPhoneNumber().equals(phonenumber));

        //currentSelectedContacts.remove(contactId);
        //Toast.makeText(getContext(),currentSelectedContacts.toString(),Toast.LENGTH_SHORT).show();
    }

    public void clearFields(){
        editTextGroupName.setText("");
        //recyclerviewCheckbox.setChecked(false);
        //adapter.notifyDataSetChanged();
        currentSelectedContacts.clear();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertGrouplist(int groupId){

        currentSelectedContacts.forEach((currentSelectedContact)->{
            boolean doesContactExist = groupListViewModel.doesContactExist(currentSelectedContact.getPhoneNumber(),groupId);
            if(doesContactExist != true){
                groupListViewModel.createGroupList(currentSelectedContact.getFirstName(),currentSelectedContact.getLastName(),"null",currentSelectedContact.getPhoneNumber(),groupId,0);

            }
        });
        Toast.makeText(getContext(),"Contact group created succesfully",Toast.LENGTH_LONG).show();
        clearFields();
        //FragmentContactGroups fragment = new FragmentContactGroups();
        //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.viewpager,fragment).commit();
        //fragmentTransaction.addToBackStack(null);
        //fragmentTransaction.commit();

        //viewPager.setCurrentItem(1);


    }


}
