package com.example.ex_contactapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.models.ModelContactGroups;

import java.util.ArrayList;
import java.util.List;

public class ContactGroupsRvAdapter extends RecyclerView.Adapter<ContactGroupsRvAdapter.ViewHolder>{
    private Context mContext;
    private LayoutInflater inflater;
    private List<ModelContactGroups> mContactGroupsList;

    public ContactGroupsRvAdapter(Context context, List<ModelContactGroups> listContactGroups){

        mContext = context;
        mContactGroupsList = new ArrayList<>();
        mContactGroupsList = listContactGroups;
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

        contact_group_name.setText(mContactGroupsList.get(position).getGroupName());
        String sizeOfGroup = String.valueOf(mContactGroupsList.get(position).getContactIdList().size());
        contact_group_numof_members.setText(sizeOfGroup);
    }

    @Override
    public int getItemCount() {
        return mContactGroupsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView contact_group_name, contact_group_numof_members;

        public ViewHolder(View itemView){
            super(itemView);

            contact_group_name = itemView.findViewById(R.id.contact_group_name);
            contact_group_numof_members = itemView.findViewById(R.id.contact_group_noOFMembers);
        }



    }
}
