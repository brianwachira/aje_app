package com.example.ex_contactapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ex_contactapp.R;
import com.example.ex_contactapp.data.Entities.Grouplist;
import java.util.List;

public class DialogContactsRvAdapter extends RecyclerView.Adapter<DialogContactsRvAdapter.ViewHolder> {
    private Context mdialogContext;
    private LayoutInflater inflater;
    private List<Grouplist> groupList;
    private DialogItemListener mdialogItemListener;


    public interface DialogItemListener{
        void onItemDeleteListener(Integer num);
    }

    public DialogContactsRvAdapter(Context context, List<Grouplist> mgrouplist, DialogItemListener dialogItemListener) {
        mdialogContext = context;
        groupList = mgrouplist;
        mdialogItemListener = dialogItemListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(mdialogContext);

        View view = inflater.inflate(R.layout.items_dialog_contacts,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  DialogContactsRvAdapter.ViewHolder holder, int position) {

        final TextView dialog_contact_name,dialog_contact_number;
        final Button dialogContactDelete;

        dialog_contact_name = holder.dialogContact_name;
        dialog_contact_number = holder.dialogContact_number;
        dialogContactDelete = holder.dialogContactDelete;

        //holder.dialogContact_name.setText("wasgood");
        dialog_contact_name.setText(groupList.get(position).getFirstName() + " " + groupList.get(position).getMiddleName() + " " + groupList.get(position).getLastName());
        dialog_contact_number.setText(groupList.get(position).getPhoneNumber());

        //dialogContactDelete.setOnClickListener((View.OnClickListener) mdialogContext.getApplicationContext());
        holder.bind(this.groupList.get(position).getContactid(), mdialogItemListener);

    }

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        TextView dialog_contact_name = holder.dialogContact_name;
//        TextView dialog_contact_number = holder.dialogContact_number;
//        Button dialogContactDelete = holder.dialogContactDelete;
//        dialog_contact_name.setText(this.groupList.get(position).getFirstName() + " " + this.groupList.get(position).getMiddleName() + " " + this.groupList.get(position).getLastName());
//        dialog_contact_number.setText(this.groupList.get(position).getPhoneNumber());
//        dialogContactDelete.setOnClickListener((View.OnClickListener) this);
//        holder.bind(this.groupList.get(position).getContactid(), this.mdialogItemListener);
//    }

    static /* synthetic */ void lambda$onBindViewHolder$0(View v) {
    }

    public int getItemCount() {
        return this.groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button dialogContactDelete;
        TextView dialogContact_name;
        TextView dialogContact_number;

        public ViewHolder(View itemView) {
            super(itemView);
            dialogContact_name =  itemView.findViewById(R.id.dialog_contact_name);
            dialogContact_number = itemView.findViewById(R.id.dialog_contact_number);
            dialogContactDelete =  itemView.findViewById(R.id.dialog_contact_delete);
        }

        public void bind(final Integer contactid, final DialogItemListener mdialogItemListener) {
            dialogContactDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mdialogItemListener.onItemDeleteListener(contactid);
                }
            });
        }
    }
}
