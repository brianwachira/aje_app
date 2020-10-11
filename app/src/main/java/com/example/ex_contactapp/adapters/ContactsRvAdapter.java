package com.example.ex_contactapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.models.ModelContacts;

import java.util.List;


public class ContactsRvAdapter extends RecyclerView.Adapter<ContactsRvAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<ModelContacts> mlistContacts;
    private CheckedStatusListener mcheckedStatusListener;


    public interface CheckedStatusListener{
        //String firstName, String lastName, String middleName, @NonNull String phoneNumber,@NonNull int groupid
        void onItemChecked(String name, String phonenumber);
        void onItemUnchecked(String name, String phonenumber);
    }

    public ContactsRvAdapter(Context context, List<ModelContacts> listContacts,CheckedStatusListener checkedStatusListener){

        mlistContacts = listContacts;

        mContext = context;
        mcheckedStatusListener = checkedStatusListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.items_contacts,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final TextView contact_name,contact_number;
        final CheckBox contact_checkbox;

        contact_name = holder.contact_name;
        contact_number = holder.contact_number;

        contact_name.setText(mlistContacts.get(position).getName());
        contact_number.setText(mlistContacts.get(position).getNumber());

        contact_checkbox = holder.contactSelectedCheckBox;


        contact_checkbox.setOnClickListener((View v) -> {
            if (contact_checkbox.isChecked()){
                //Toast.makeText(mContext,mlistContacts.get(position).getId(),Toast.LENGTH_LONG).show();
                //String firstName, String lastName, String middleName, @NonNull String phoneNumber,@NonNull int groupid
                mcheckedStatusListener.onItemChecked(mlistContacts.get(position).getName(),mlistContacts.get(position).getNumber());
            }else{
                mcheckedStatusListener.onItemUnchecked(mlistContacts.get(position).getName(),mlistContacts.get(position).getNumber());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlistContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView contact_name,contact_number;
        CheckBox contactSelectedCheckBox;

        public ViewHolder(View itemView){
            super(itemView);

            contact_name = itemView.findViewById(R.id.contact_name);
            contact_number = itemView.findViewById(R.id.contact_number);
            contactSelectedCheckBox = itemView.findViewById(R.id.contact_checkbox);

        }

    }
}
