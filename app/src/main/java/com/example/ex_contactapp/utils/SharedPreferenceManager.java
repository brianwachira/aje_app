package com.example.ex_contactapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.ex_contactapp.LoginActivity;
import com.example.ex_contactapp.data.User;

public class SharedPreferenceManager {

    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_ID = "keyid";
    private static final String KEY_USER = "keyusername";
    private static final String KEY_GIVENID = "keygivenid";
    private static SharedPreferenceManager mInstance;
    private static Context ctx;

    private SharedPreferenceManager(Context context){
        ctx = context;
    }

    public static synchronized SharedPreferenceManager getInstance(Context context){
        if (mInstance == null) {
            mInstance = new SharedPreferenceManager(context);
        }
        return mInstance;

    }

    //this method will store the user data in shared preferences

    public void userLogin(User user){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID,user.getId());
        editor.putString(KEY_USER,user.getGivenName());
        editor.putString(KEY_GIVENID,user.getGivenId());
        editor.apply();
    }

    //this method will check whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER, null) != null;
    }

    //this method will give the logged in user
    public User getUser(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new User(
               sharedPreferences.getInt(KEY_ID,-1),
               sharedPreferences.getString(KEY_USER,null),
                sharedPreferences.getString(KEY_GIVENID,null)

        );
    }

    //this method will logout the user
    public void logout(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginActivity.class));
    }

}
