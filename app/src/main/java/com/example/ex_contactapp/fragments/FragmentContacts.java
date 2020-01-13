package com.example.ex_contactapp.fragments;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.adapters.ContactsRvAdapter;
import com.example.ex_contactapp.models.ModelContacts;
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

    private Button buttonCreateGroup;

    private SharedViewModel sharedViewModel;
    public FragmentContacts() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_contacts,container,false);

        recyclerView = v.findViewById(R.id.rv_contacts);

        buttonCreateGroup = v.findViewById(R.id.button_create_group);

        editTextGroupName = v.findViewById(R.id.group_name);

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
            buttonCreateGroup.setOnClickListener(v -> {

                sharedViewModel.setGroupName(editTextGroupName.getText().toString());
                sharedViewModel.setCurrentSelectedContacts(currentSelectedContacts);

                //clearFields();
                editTextGroupName.setText("");
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
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

            Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,null,null,ContactsContract.Contacts.DISPLAY_NAME + " ASC");
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
        currentSelectedContacts.clear();

    }

}
