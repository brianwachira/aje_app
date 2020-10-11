package com.example.ex_contactapp.fragments;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Toast;
        import androidx.fragment.app.DialogFragment;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentActivity;
        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProvider;
        import androidx.lifecycle.ViewModelProviders;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import com.example.ex_contactapp.R;
        import com.example.ex_contactapp.adapters.DialogContactsRvAdapter;
        import com.example.ex_contactapp.data.Entities.ContactGroup;
        import com.example.ex_contactapp.data.Entities.Grouplist;
        import com.example.ex_contactapp.data.Relations.ContactGroupAndGroupList;
        import com.example.ex_contactapp.viewmodels.ContactGroupViewModel;
        import com.example.ex_contactapp.viewmodels.GroupListViewModel;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Objects;

public class FragmentDialogContacts extends DialogFragment implements DialogContactsRvAdapter.DialogItemListener {
    private static List<Grouplist> currentSelectedContacts = new ArrayList();
    private static String dialogTitle;
    private static Integer groupId;
    DialogContactsRvAdapter adapter;
    LiveData<ContactGroupAndGroupList> contactGroupAndGroupList;
    ContactGroupViewModel contactGroupViewModel;
    GroupListViewModel groupListViewModel;
    private RecyclerView recyclerView;
    ContactGroup selectedContactGroup;

    /* renamed from: v */
    private View f51v;

    public static FragmentDialogContacts newInstance(int id) {
        FragmentDialogContacts fragmentDialogContacts = new FragmentDialogContacts();
        Bundle args = new Bundle();
        groupId = Integer.valueOf(id);
        args.putString("title", dialogTitle);
        fragmentDialogContacts.setArguments(args);
        return fragmentDialogContacts;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.dialog_contacts, container, false);
        this.f51v = inflate;
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_dialog_contacts);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.groupListViewModel = (GroupListViewModel) ViewModelProviders.of((Fragment) this, (ViewModelProvider.Factory) new GroupListViewModel.Factory(((FragmentActivity) Objects.requireNonNull(getActivity())).getApplicationContext())).get(GroupListViewModel.class);
        ContactGroupViewModel contactGroupViewModel2 = (ContactGroupViewModel) ViewModelProviders.of((Fragment) this, (ViewModelProvider.Factory) new ContactGroupViewModel.Factory(getActivity().getApplicationContext())).get(ContactGroupViewModel.class);
        this.contactGroupViewModel = contactGroupViewModel2;
        LiveData<ContactGroupAndGroupList> readGroupAndContacts = contactGroupViewModel2.readGroupAndContacts(groupId);
        this.contactGroupAndGroupList = readGroupAndContacts;

        this.contactGroupAndGroupList.observe(this, contactGroupAndGroupLists ->{
            setContactDetailsNeeded(contactGroupAndGroupLists.getContactGroup(),contactGroupAndGroupLists.getGrouplist());

        });

        return this.f51v;
    }


    public void setContactDetailsNeeded(ContactGroup contactGroup, List<Grouplist> grouplist) {
        this.selectedContactGroup = contactGroup;
        dialogTitle = contactGroup.getGroupname();
        currentSelectedContacts = grouplist;
        setAdapter(new DialogContactsRvAdapter(getContext(), currentSelectedContacts, this));
    }

    private void setAdapter(DialogContactsRvAdapter adapter2) {
        this.recyclerView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onItemDeleteListener(Integer contactId) {
        final int contactToDelete = contactId.intValue();
        new AlertDialog.Builder(getActivity()).setIcon(R.drawable.ic_delete).setTitle("Are you sure").setMessage("Do you want to delete this group").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FragmentDialogContacts.this.groupListViewModel.deleteContactFromGroup(contactToDelete);
                Toast.makeText(FragmentDialogContacts.this.getContext(), "Contact deleted", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("No", (DialogInterface.OnClickListener) null).show();
    }
}

