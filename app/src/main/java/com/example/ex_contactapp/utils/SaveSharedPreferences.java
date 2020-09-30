package com.example.ex_contactapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import static com.example.ex_contactapp.utils.PreferenceUtility.LOGGED_IN_PREF;
import static com.example.ex_contactapp.utils.PreferenceUtility.givenName;
import static com.example.ex_contactapp.utils.PreferenceUtility.givenId;
public class SaveSharedPreferences {

    static SharedPreferences getPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Set Account id
     * @param context
     * @param id
     */
    public  static void setId(Context context, String id){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(givenId,id);
    }

    /**
     * Set user's name
     * @param context
     * @param name
     */
    public static void setName(Context context,String name){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(givenName,name);
    }

    /**
     * Set the login status
     * @param context
     * @param loggedIn
     */
    public static void setLoggedIn(Context context,boolean loggedIn){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF,loggedIn);
        editor.apply();

    }
    /**
     * Get user id
     * @param context
     * @return String: id
     */
    public static String getGivenId(Context context){
        return getPreferences(context).getString(givenId,"id");
    }

    /**
     * Get given name
     * @param context
     * @return String: name
     */
    public static String getGivenName(Context context){
        return getPreferences(context).getString(givenName,"name");
    }

    /**
     * Get the Login Status
     * @param context
     * @return boolean: login status
     */
    public static boolean getLoggedStatus(Context context){
        return getPreferences(context).getBoolean(LOGGED_IN_PREF,false);
    }
}
