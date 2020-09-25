package com.example.ex_contactapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    //private RecyclerViewLongClickedListener mRecyclerViewLongClickedListener;


    public ContactGroupsRvAdapter(Context context, List<ContactGroup> listContactGroups, RecyclerViewTappedListener recyclerViewTappedListener/*, RecyclerViewLongClickedListener recyclerViewLongClickedListener*/){

        mContext = context;
        mContactGroupsList = new ArrayList<>();
        contactGroupLiveData = listContactGroups;
        mRecyclerViewClickedListener = recyclerViewTappedListener;
        //mRecyclerViewLongClickedListener = recyclerViewLongClickedListener;

    }

    public interface RecyclerViewTappedListener{
        void onRecyclerViewTapped(int id);
        void onRecyclerViewLongClick(int id);
    }

    public interface RecyclerViewLongClickedListener{
        void onRecyclerViewLongClick(int id);

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

        holder.bind(contactGroupLiveData.get(position).getId(),mRecyclerViewClickedListener);

        //holder.bindanother(Integer.parseInt(contactGroupLiveData.get(position).getId().toString()),mRecyclerViewLongClickedListener);
    }

    @Override
    public int getItemCount() {
        return contactGroupLiveData == null ? 0 : contactGroupLiveData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView contact_group_name, contact_group_numof_members;

        public ViewHolder(View itemView){
            super(itemView);

            contact_group_name = itemView.findViewById(R.id.contact_group_name);
            contact_group_numof_members = itemView.findViewById(R.id.contact_group_noOFMembers);

        }




        public void bind(final Integer id,  final RecyclerViewTappedListener mListener){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRecyclerViewTapped(id);
                    //Toast.makeText(mContext, contactIdList + " ", Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    mListener.onRecyclerViewLongClick(id);
                    return false;
                }
            });
        }
    }
}
