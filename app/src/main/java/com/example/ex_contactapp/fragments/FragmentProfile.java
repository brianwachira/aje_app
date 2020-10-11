package com.example.ex_contactapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.ex_contactapp.LoginActivity;
import com.example.ex_contactapp.R;
import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Entities.Grouplist;
import com.example.ex_contactapp.data.User;
import com.example.ex_contactapp.utils.SaveSharedPreferences;
import com.example.ex_contactapp.utils.SharedPreferenceManager;
import com.example.ex_contactapp.viewmodels.ContactGroupViewModel;
import com.example.ex_contactapp.viewmodels.GroupListViewModel;
import com.example.ex_contactapp.volley.URLs;
import com.example.ex_contactapp.volley.VolleySingleton;
import com.example.ex_contactapp.volley.VolleyContactGroup;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentProfile extends Fragment {
    private View v;
    String name = " ";
    TextView accountName;

    ContactGroupViewModel contactGroupViewModel;
    GroupListViewModel groupListViewModel;

    Button btnSync;
    List<Grouplist> grouplistById;
    List<Grouplist> grouplistWithRemote;
    List<ContactGroup> contactGroupsForSync;
    List<Grouplist> groupListForSync;
    User user;
    int apiGroupId;

    VolleyContactGroup volleycontactGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile,container,false);

         accountName = v.findViewById(R.id.accountName);

        btnSync = v.findViewById(R.id.buttonSync);
        name = SaveSharedPreferences.getGivenName(getContext());
        accountName.setText(name);
        contactGroupsForSync = new ArrayList<>();
        groupListForSync = new ArrayList<>();
        grouplistById = new ArrayList<>();
        grouplistWithRemote = new ArrayList<>();
        if(SharedPreferenceManager.getInstance(getContext()).isLoggedIn()){
            user = SharedPreferenceManager.getInstance(getContext()).getUser();

            accountName.setText(user.getGivenName() + " " + user.getId());
        }else{
            Intent intent = new Intent(v.getContext(),LoginActivity.class);
            startActivity(intent);

        }

        contactGroupViewModel = ViewModelProviders.of(this, new ContactGroupViewModel.Factory(getActivity().getApplicationContext())).get(ContactGroupViewModel.class);

        groupListViewModel = ViewModelProviders.of(this,new GroupListViewModel.Factory(getActivity().getApplicationContext())).get(GroupListViewModel.class);

        for(ContactGroup contactGroup :contactGroupViewModel.readGroupForSync()){

            contactGroupsForSync.add(new ContactGroup(contactGroup.getId(),contactGroup.getGroupname(),contactGroup.getNumofcontacts())) ;
        }

        for(Grouplist grouplist : groupListViewModel.getGroupListForSync()){
            groupListForSync.add(new Grouplist(grouplist.getFirstName(),grouplist.getLastName(),grouplist.getMiddleName(),grouplist.getPhoneNumber(),grouplist.getGroupid(),grouplist.getRemotegroupid()));
        }

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               synContacts();
            }
        });
        return v;
    }

    private void synContacts() {

        for(ContactGroup contactGroup : contactGroupsForSync){

//            for(Grouplist grouplist : groupListViewModel.readGroupListById(contactGroup.getId())){
//                    groupListForSync.add(new Grouplist(grouplist.getFirstName(),grouplist.getLastName(),grouplist.getMiddleName(),grouplist.getPhoneNumber(),grouplist.getGroupid(),grouplist.getRemotegroupid()));
//                }
            syncContactsbyClass(contactGroup);


            //for(Grouplist grouplist : groupListForSync){
                //syncGrouplist(grouplist,apiGroupId);
                //grouplistWithRemote.add(new Grouplist(grouplist.getFirstName(),grouplist.getLastName(),grouplist.getMiddleName(),grouplist.getPhoneNumber(),grouplist.getGroupid(),apiGroupId));
            //}
            //Toast.makeText(getContext(),apiGroupId+" is the group id",Toast.LENGTH_SHORT).show();


//            for(Grouplist grouplist: grouplistWithRemote){
//                syncGrouplist(grouplist,apiGroupId);
//            }
        }

        for(Grouplist grouplist : groupListViewModel.getGroupListForSync()){
            //Toast.makeText(getContext(),grouplist.getRemotegroupid()+" ",Toast.LENGTH_SHORT).show();
            syncGrouplist(grouplist);
        }

    }


    public void syncContactsbyClass(ContactGroup contactGroup){
        //Toast.makeText(getContext(),contactGroup.getGroupname(),Toast.LENGTH_SHORT).show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CREATECONTACTGROUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("response",response);
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                apiGroupId = userJson.getInt("id");
                                //creating a new user object

                                volleycontactGroup = new VolleyContactGroup(
////                                        'id'=>$id,
////                                'groupname'=>$groupname,
////                                        'numofcontacts'=>$numofcontacts,
////                                        'userid'=>$userid
                                      userJson.getInt("id"),
                                        userJson.getString("groupname"),
                                        userJson.getString("numofcontacts"),
                                        userJson.getInt("userid")
                                );

                                groupListViewModel.updateGrouplistRemoteid(userJson.getInt("id"),contactGroup.getId());
                                Toast.makeText(getContext(), obj.getString("message") + "ID: " + volleycontactGroup.getId(), Toast.LENGTH_SHORT).show();

//                                Toast.makeText(getContext(), contactGroup.getGroupname(), Toast.LENGTH_SHORT).show();


//                                //storing the user in shared preferences
//                                SharedPreferenceManager.getInstance(getContext()).userLogin(user);
//
//                                //start the main activity
//                                Intent mainActivityIntent = new Intent(getContext(), MainActivity.class);
//                                startActivity(mainActivityIntent);


                            } else {
                                Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {


                    Map<String,String> params = new HashMap<>();
                    params.put("groupname",contactGroup.getGroupname());
                    params.put("numofcontacts",contactGroup.getNumofcontacts());
                    params.put("userid",user.getId()+"");
                    //'id','groupname','numofcontacts','userid'

                    return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }



    private void syncGrouplist(Grouplist grouplist){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CREATEGROUPLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("response",response);
                            //converting response to json object
                            JSONObject obj2 = new JSONObject(response);
                            //if no error in response
                            if (!obj2.getBoolean("error")) {
                                Toast.makeText(getContext(), obj2.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj2.getJSONObject("user");

                                //apiGroupId = userJson.getInt("id");
                                //creating a new user object

//                                ContactGroup contactGroup = new ContactGroup(
////                                        'id'=>$id,
////                                'groupname'=>$groupname,
////                                        'numofcontacts'=>$numofcontacts,
////                                        'userid'=>$userid
//                                        userJson.getInt("id"),
//                                        userJson.getString("groupname"),
//                                        userJson.getString("numofcontacts")
//                                );
//                                Toast.makeText(getContext(), contactGroup.getGroupname(), Toast.LENGTH_SHORT).show();


//                                //storing the user in shared preferences
//                                SharedPreferenceManager.getInstance(getContext()).userLogin(user);
//
//                                //start the main activity
//                                Intent mainActivityIntent = new Intent(getContext(), MainActivity.class);
//                                startActivity(mainActivityIntent);


                            } else {
                                Toast.makeText(getContext(), obj2.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {

                //'firstname','lastname','phonenumber','groupid','middlename'
                Map<String,String> params = new HashMap<>();
                params.put("firstname",grouplist.getFirstName());
                params.put("lastname",grouplist.getLastName());
                params.put("phonenumber",grouplist.getPhoneNumber());
                params.put("groupid",grouplist.getRemotegroupid()+"");
                params.put("middlename",grouplist.getMiddleName());
                //'id','groupname','numofcontacts','userid'

                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
