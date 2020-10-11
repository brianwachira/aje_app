package com.example.ex_contactapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.adapters.ContactGroupsRvAdapter;
import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.models.ModelContactGroups;
import com.example.ex_contactapp.viewmodels.ContactGroupViewModel;
import com.example.ex_contactapp.viewmodels.SharedViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentContactGroups extends Fragment implements ContactGroupsRvAdapter.RecyclerViewTappedListener {

    private View v;

    private RecyclerView recyclerView;

    SharedViewModel sharedViewModel;

    ContactGroupsRvAdapter adapter;

    ContactGroupViewModel contactGroupViewModel;

    List<ContactGroup> contactGroup;

    public FragmentContactGroups() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_contact_groups,container,false);

        recyclerView = v.findViewById(R.id.rv_contact_groups);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        contactGroupViewModel = ViewModelProviders.of(this, new ContactGroupViewModel.Factory(getActivity().getApplicationContext())).get(ContactGroupViewModel.class);

         contactGroupViewModel.readGroup().observe(this,contactGroups ->
                //contactGroup = contactGroups);


                 setAdapter(new ContactGroupsRvAdapter(getContext(),contactGroups,this))
         );

        return v;
    }

    /*private List<ModelContactGroups> getContactGroups(){
        List<ModelContactGroups> list = new ArrayList<>();
        sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
            sharedViewModel.getCurrentSelectedContacts().observe(this,currentSelectedContacts ->{
                sharedViewModel.getGroupName().observe(this,groupName -> {

                list.add(new ModelContactGroups("ab",groupName,currentSelectedContacts));
            });

        });
        return list;
    }*/
    void setAdapter(ContactGroupsRvAdapter adapter){
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onRecyclerViewTapped(int id) {
        showDialog( id);
    }

    @Override
    public void onRecyclerViewLongClick(int id) {
        final int groupToDelete = id;

        new AlertDialog.Builder(this.getActivity())
                .setIcon(R.drawable.ic_delete)
                .setTitle("Are you sure")
                .setMessage("Do you want to delete this group")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactGroupViewModel.deleteGroup(groupToDelete);
                        Toast.makeText(getContext(),"Group deleted",Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton("No",null)
                .show();

    }

    private void showDialog(int id){
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentDialogContacts fragmentDialogContacts = FragmentDialogContacts.newInstance(id);
        fragmentDialogContacts.setTargetFragment(this,0);
        fragmentDialogContacts.show(fm,"contact-group-list");
    }
}
