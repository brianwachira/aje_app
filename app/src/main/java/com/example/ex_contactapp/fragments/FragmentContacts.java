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
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.adapters.ContactsRvAdapter;
import com.example.ex_contactapp.models.ModelContacts;
import com.example.ex_contactapp.viewmodels.ContactGroupViewModel;
import com.example.ex_contactapp.viewmodels.SharedViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FragmentContacts extends Fragment implements ContactsRvAdapter.CheckedStatusListener{

    private  View v;

    private RecyclerView recyclerView;

    private List<String> currentSelectedContacts = new ArrayList<>();

    ContactsRvAdapter adapter;

    private EditText editTextGroupName;

    private CheckBox recyclerviewCheckbox;

    private Button buttonCreateGroup;

    private SharedViewModel sharedViewModel;

    private ContactGroupViewModel contactGroupViewModel;

    public FragmentContacts() {

    }

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

            buttonCreateGroup.setOnClickListener(v -> {

                Log.i("editTextGroupNamelength", String.valueOf(editTextGroupName.getText().toString().length()));
                if (editTextGroupName.getText().toString().length() > 2) {
                    if(currentSelectedContacts.size() > 4){
                        contactGroupViewModel.createGroup(editTextGroupName.getText().toString(),String.valueOf(currentSelectedContacts.size()));
                        clearFields();
                    }else{
                        new AlertDialog.Builder(this.getActivity())
                                .setIcon(R.drawable.ic_error)
                                .setTitle("Insufficient group members")
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

                //sharedViewModel.setGroupName(editTextGroupName.getText().toString());
                //sharedViewModel.setCurrentSelectedContacts(currentSelectedContacts);

                //clearFields();
                //editTextGroupName.setText("");

               // Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
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
    public void onItemChecked(String contactId) {
        currentSelectedContacts.add(contactId);
        //Toast.makeText(getContext(),currentSelectedContacts.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemUnchecked(String contactId) {
        currentSelectedContacts.remove(contactId);
        //Toast.makeText(getContext(),currentSelectedContacts.toString(),Toast.LENGTH_SHORT).show();
    }

    public void clearFields(){
        editTextGroupName.setText("");
        //recyclerviewCheckbox.setChecked(false);
        //adapter.notifyDataSetChanged();
        currentSelectedContacts.clear();


    }


}
