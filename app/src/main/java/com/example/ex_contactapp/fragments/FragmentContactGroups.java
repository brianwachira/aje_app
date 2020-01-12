package com.example.ex_contactapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.adapters.ContactGroupsRvAdapter;
import com.example.ex_contactapp.models.ModelContactGroups;
import com.example.ex_contactapp.viewmodels.SharedViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentContactGroups extends Fragment {

    private View v;

    private RecyclerView recyclerView;

    SharedViewModel sharedViewModel;

    ContactGroupsRvAdapter adapter;
    public FragmentContactGroups() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_contact_groups,container,false);

        recyclerView = v.findViewById(R.id.rv_contact_groups);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        adapter = new ContactGroupsRvAdapter(getContext(),getContactGroups());

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        return v;
    }

    private List<ModelContactGroups> getContactGroups(){
        List<ModelContactGroups> list = new ArrayList<>();
        sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);

            sharedViewModel.getCurrentSelectedContacts().observe(this,currentSelectedContacts ->{
                sharedViewModel.getGroupName().observe(this,groupName -> {

                list.add(new ModelContactGroups("ab",groupName,currentSelectedContacts));
            });

        });

        return list;
    }
}
