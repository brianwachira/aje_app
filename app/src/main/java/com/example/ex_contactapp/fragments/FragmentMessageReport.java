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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.adapters.MessageReportAdapter;
import com.example.ex_contactapp.viewmodels.MessageViewModel;

public class FragmentMessageReport extends Fragment implements MessageReportAdapter.RecyclerViewTappedListener {

    private View v;
    private RecyclerView recyclerView;

    MessageReportAdapter messageReportAdapter;
    MessageViewModel messageViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_message_report,container,false);

        recyclerView = v.findViewById(R.id.rv_message_report);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        messageViewModel= ViewModelProviders.of(this,new MessageViewModel.Factory(getActivity().getApplicationContext())).get(MessageViewModel.class);

        messageViewModel.readMessages().observe(this,messages ->
                setAdapter(new MessageReportAdapter(getContext(),messages,this))
                );

        return v;
    }

    private void setAdapter(MessageReportAdapter messageReportAdapter) {
        recyclerView.setAdapter(messageReportAdapter);
        messageReportAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecyclerViewTapped(int id) {

    }

    @Override
    public void onRecyclerViewLongClick(int id) {
        final int messageReportToDelete = id;

        new AlertDialog.Builder(this.getActivity())
                .setIcon(R.drawable.ic_delete)
                .setTitle("Are you sure")
                .setMessage("Do you want to delete this group")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        messageViewModel.deleteMessage(messageReportToDelete);
                        Toast.makeText(getContext(),"Group deleted",Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton("No",null)
                .show();
    }
}
