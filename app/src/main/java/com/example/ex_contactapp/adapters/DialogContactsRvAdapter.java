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
import com.example.ex_contactapp.models.ModelDialogContacts;

import java.util.List;

public class DialogContactsRvAdapter extends RecyclerView.Adapter<DialogContactsRvAdapter.ViewHolder> {

    private  Context mdialogContext;
    private LayoutInflater inflater;
    private List<ModelDialogContacts> mdialogContacts;
    private DialogCheckedStatusListener mdialogCheckedStatusListener;

    public interface DialogCheckedStatusListener{
        void onItemChecked(String contactId);
        void onItemUnchecked(String contactId);

    }

    public DialogContactsRvAdapter(Context context, List<ModelDialogContacts> listDialogContacts,DialogCheckedStatusListener dialogCheckedStatusListener){
        mdialogContacts = listDialogContacts;
        mdialogContext = context;
        mdialogCheckedStatusListener = dialogCheckedStatusListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        inflater = LayoutInflater.from(mdialogContext);
        View view = inflater.inflate(R.layout.items_dialog_contacts,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final TextView dialog_contact_name,dialog_contact_number;
        final CheckBox dialog_contact_checkbox;

        dialog_contact_name = holder.dialogContact_name;
        dialog_contact_number = holder.dialogContact_number;

        dialog_contact_checkbox = holder.dialogContactSelectedCheckBox;

        dialog_contact_name.setText(mdialogContacts.get(position).getName());
        dialog_contact_number.setText(mdialogContacts.get(position).getNumber());

        dialog_contact_checkbox.setOnClickListener(v -> {
            if(dialog_contact_checkbox.isChecked()){
                mdialogCheckedStatusListener.onItemChecked(mdialogContacts.get(position).getId());
            }else{
                mdialogCheckedStatusListener.onItemUnchecked(mdialogContacts.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdialogContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView dialogContact_name,dialogContact_number;
        CheckBox dialogContactSelectedCheckBox;

        public  ViewHolder(View itemView){
            super(itemView);

            dialogContact_name = itemView.findViewById(R.id.dialog_contact_name);
            dialogContact_number = itemView.findViewById(R.id.dialog_contact_number);
            dialogContactSelectedCheckBox = itemView.findViewById(R.id.dialog_contact_checkbox);
        }
    }

}
