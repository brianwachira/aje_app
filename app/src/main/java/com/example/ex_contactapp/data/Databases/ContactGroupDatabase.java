package com.example.ex_contactapp.data.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ex_contactapp.data.DAO.ContactGroupDao;
import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Entities.Grouplist;
import com.example.ex_contactapp.data.Entities.Message;

@Database(entities = {ContactGroup.class, Grouplist.class, Message.class}, version = 1)
public abstract class ContactGroupDatabase extends RoomDatabase {

    public abstract ContactGroupDao contactGroupDao();
    public static ContactGroupDatabase INSTANCE;

    public static ContactGroupDatabase getAppDatabase(Context context){

        if(INSTANCE == null){

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),ContactGroupDatabase.class,"contactgroup").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
    public static void destroyInstance(){INSTANCE=null;}}