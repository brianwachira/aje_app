package com.example.ex_contactapp.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ex_contactapp.data.Databases.ContactGroupDatabase;
import com.example.ex_contactapp.data.Entities.Message;
import com.example.ex_contactapp.data.Repositories.MessageRepository;

import java.util.List;

public class MessageViewModel extends ViewModel {
    private MessageRepository messageRepository;

    public MessageViewModel(Context context){
        messageRepository = MessageRepository.getInstance(ContactGroupDatabase.getAppDatabase(context).messageDAO());
    }

    public void createMessage(String messageContent, int groupId){
        messageRepository.insertMessage(messageContent,groupId);
    }

    public  void deleteMessage(int messageId){
        messageRepository.deleteMessage(messageId);
    }

    public LiveData<List<Message>> readMessages(){
        return messageRepository.ReadMessages();
    }
    public LiveData<Message>readMessageById(Integer id){
        return messageRepository.getMessagesById(id);
    }
    public static class Factory implements ViewModelProvider.Factory{
        private final Context context;

        public Factory(Context context){
            this.context = context.getApplicationContext();
        }


        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MessageViewModel(context);
        }
    }
}
