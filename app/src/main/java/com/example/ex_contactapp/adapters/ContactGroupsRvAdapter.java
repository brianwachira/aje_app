package com.example.ex_contactapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.models.ModelContactGroups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactGroupsRvAdapter extends RecyclerView.Adapter<ContactGroupsRvAdapter.ViewHolder>{
    private Context mContext;
    private LayoutInflater inflater;
    private List<ModelContactGroups> mContactGroupsList;
    private List<ContactGroup> contactGroupLiveData;
    private RecyclerViewTappedListener mRecyclerViewClickedListener;

    public ContactGroupsRvAdapter(Context context, List<ContactGroup> listContactGroups, RecyclerViewTappedListener recyclerViewTappedListener){

        mContext = context;
        mContactGroupsList = new ArrayList<>();
        contactGroupLiveData = listContactGroups;
        mRecyclerViewClickedListener = recyclerViewTappedListener;
    }

    public interface RecyclerViewTappedListener{
        void onRecyclerViewTapped(String groupName,List<String> contactIdList);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.item_contact_groups,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactGroupsRvAdapter.ViewHolder holder, int position) {

        final TextView contact_group_name, contact_group_numof_members;

        contact_group_name = holder.contact_group_name;
        contact_group_numof_members = holder.contact_group_numof_members;


        contact_group_name.setText(contactGroupLiveData.get(position).getGroupname());
        //String sizeOfGroup = String.valueOf(mContactGroupsList.get(position).getContactIdList().size());
        contact_group_numof_members.setText(contactGroupLiveData.get(position).getNumofcontacts());

        holder.bind(contactGroupLiveData.get(position).getGroupname(), Collections.singletonList(String.valueOf(contactGroupLiveData.get(position).getId())),mRecyclerViewClickedListener);
    }

    @Override
    public int getItemCount() {
        return contactGroupLiveData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView contact_group_name, contact_group_numof_members;

        public ViewHolder(View itemView){
            super(itemView);

            contact_group_name = itemView.findViewById(R.id.contact_group_name);
            contact_group_numof_members = itemView.findViewById(R.id.contact_group_noOFMembers);

        }




        public void bind(final String groupName,final List<String> contactIdList, final RecyclerViewTappedListener mListener){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRecyclerViewTapped(groupName,contactIdList);
                    //Toast.makeText(mContext, contactIdList + " ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
