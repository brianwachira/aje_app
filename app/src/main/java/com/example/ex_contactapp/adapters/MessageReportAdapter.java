package com.example.ex_contactapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ex_contactapp.R;
import com.example.ex_contactapp.data.Entities.Message;
import com.example.ex_contactapp.models.ModelMessages;

import java.util.ArrayList;
import java.util.List;

public class MessageReportAdapter extends RecyclerView.Adapter<MessageReportAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ModelMessages> modelMessages;
    private List<Message> messagesLiveData;
    private RecyclerViewTappedListener mRecyclerViewClickedListener;

    public MessageReportAdapter(Context context, List<Message> messages,RecyclerViewTappedListener recylerViewTappedListener){
        mContext = context;
        modelMessages = new ArrayList<>();
        messagesLiveData = messages;
        mRecyclerViewClickedListener = recylerViewTappedListener;

    }

    public interface RecyclerViewTappedListener{
        void onRecyclerViewTapped(int id);
        void onRecyclerViewLongClick(int id);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        inflater= LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_message_report,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageReportAdapter.ViewHolder holder, int position) {

        final TextView reportMessage, reportGroupName;

        reportMessage = holder.textViewMessage;
        reportGroupName = holder.textViewGroupName;

        reportMessage.setText(messagesLiveData.get(position).getMessageContent());
        reportGroupName.setText(messagesLiveData.get(position).getGroupid() + " ");

        holder.bind(messagesLiveData.get(position).getMessageid(),mRecyclerViewClickedListener);
    }

    @Override
    public int getItemCount() {
        return messagesLiveData== null?0:messagesLiveData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewMessage, textViewGroupName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewMessage = itemView.findViewById(R.id.messageReportMessage);
            textViewGroupName = itemView.findViewById(R.id.messageReportGroupName);
        }
        public void bind(final Integer id, final RecyclerViewTappedListener recyclerViewTappedListener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewTappedListener.onRecyclerViewTapped(id);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    recyclerViewTappedListener.onRecyclerViewLongClick(id);
                    return false;
                }
            });
        }
    }
}
