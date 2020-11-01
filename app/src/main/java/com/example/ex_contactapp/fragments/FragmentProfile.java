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
import com.example.ex_contactapp.adapters.ContactGroupsRvAdapter;
import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Entities.Grouplist;
import com.example.ex_contactapp.data.Entities.Message;
import com.example.ex_contactapp.data.User;
import com.example.ex_contactapp.utils.SaveSharedPreferences;
import com.example.ex_contactapp.utils.SharedPreferenceManager;
import com.example.ex_contactapp.viewmodels.ContactGroupViewModel;
import com.example.ex_contactapp.viewmodels.GroupListViewModel;
import com.example.ex_contactapp.viewmodels.MessageViewModel;
import com.example.ex_contactapp.volley.URLs;
import com.example.ex_contactapp.volley.VolleySingleton;
import com.example.ex_contactapp.volley.VolleyContactGroup;


import org.json.JSONArray;
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
    MessageViewModel messageViewModel;

    Button btnSync;
    Button btnRestore;
    Button btngoLogIn;

    Button logout;
    List<Grouplist> grouplistById;
    List<Grouplist> grouplistWithRemote;
    List<ContactGroup> contactGroupsForSync;
    List<Grouplist> groupListForSync;
    List<Message> messageList;

    List <ContactGroup> restoredContactGroup;
    List<Grouplist> restoredGroupList;
    List<Message> restoredMessageList;
    User user;
    int apiGroupId;

    JSONArray jsonArraycontactGroups;
    JSONArray jsonArraygrouplist;
    JSONArray jsonArraymessagelist;
    TextView nameTextView;

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
        messageList = new ArrayList<>();

        restoredContactGroup = new ArrayList<>();
        restoredGroupList = new ArrayList<>();
        restoredMessageList = new ArrayList<>();

        jsonArraycontactGroups = null;
        btnRestore = v.findViewById(R.id.buttonRestore);
        btngoLogIn = v.findViewById(R.id.goLogin);
        logout = v.findViewById(R.id.buttonLogout);
        nameTextView = v.findViewById(R.id.name);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceManager.getInstance(getContext()).logout();
//                Intent intent = new Intent(v.getContext(),LoginActivity.class);
//                startActivity(intent);
            }
        });
        btngoLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });


        if(SharedPreferenceManager.getInstance(getContext()).isLoggedIn()){
            user = SharedPreferenceManager.getInstance(getContext()).getUser();

            accountName.setText(user.getGivenName());
            btngoLogIn.setVisibility(View.INVISIBLE);
        }else{
            logout.setVisibility(View.INVISIBLE);
            btnSync.setVisibility(View.INVISIBLE);
            btnRestore.setVisibility(View.INVISIBLE);
            accountName.setVisibility(View.INVISIBLE);
            nameTextView.setVisibility(View.INVISIBLE);

        }

        contactGroupViewModel = ViewModelProviders.of(this, new ContactGroupViewModel.Factory(getActivity().getApplicationContext())).get(ContactGroupViewModel.class);

        groupListViewModel = ViewModelProviders.of(this,new GroupListViewModel.Factory(getActivity().getApplicationContext())).get(GroupListViewModel.class);

        messageViewModel = ViewModelProviders.of(this,new MessageViewModel.Factory(getActivity().getApplicationContext())).get(MessageViewModel.class);

        for(ContactGroup contactGroup :contactGroupViewModel.readGroupForSync()){

            contactGroupsForSync.add(new ContactGroup(contactGroup.getId(),contactGroup.getGroupname(),contactGroup.getNumofcontacts(),contactGroup.getRemoteId())) ;
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
        
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreContacts();
            }
        });
        return v;
    }

    private void restoreContacts() {

        //restore contactgroup
        restoreContactGroup();

        contactGroupViewModel.readGroup().observe(this,contactGroups ->
                //contactGroup = contactGroups);

                restoreGroupListsToPush(contactGroups)
        );

    }

    private void restoreGroupListsToPush(List<ContactGroup> contactGroups) {

        //restore grouplist
        for (ContactGroup contactGroup : contactGroups){

            restoreGroupList(contactGroup.getRemoteId(),contactGroup.getId());
        }

        for(ContactGroup contactGroup: contactGroups){
            restoreMessages(contactGroup.getRemoteId(),contactGroup.getId());
        }

    }

    private void restoreMessages(Integer remoteId2, Integer id2) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETALLMESSAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("response",response);
                            //converting response to json object
                            JSONObject obj2 = new JSONObject(response);
                            if (!obj2.getBoolean("error")) {
                                //if no error in response

                                jsonArraymessagelist = obj2.getJSONArray("messageList");

                                //loop through all grouplist json

                                for(int i = 0; i < jsonArraymessagelist.length(); i++){
                                    JSONObject c = jsonArraymessagelist.getJSONObject(i);

                                    restoredMessageList.add(new Message(c.getString("messagecontent"),id2,c.getInt("groupid")));

                                }

                                for (Message message : restoredMessageList){
                                    boolean doesMessageExist = messageViewModel.doesMessageExist(message.getMessageContent(),message.getGroupid());
                                    if(doesMessageExist == true){
                                        Toast.makeText(getContext(), "Message exists", Toast.LENGTH_SHORT).show();

                                   }else{
                                        messageViewModel.createMessage(message.getMessageContent(),message.getGroupid(),message.getGroupid());
                                    }
                                }
                                Toast.makeText(getContext(), obj2.getString("message"), Toast.LENGTH_SHORT).show();

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
                //''messagecontent','groupid'
                Map<String,String> params = new HashMap<>();
                params.put("groupid",remoteId2+"");

                return params;
            }

        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    private void restoreGroupList(int remoteId1,int id1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETALLGROUPLISTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("response",response);
                            //converting response to json object
                            JSONObject obj2 = new JSONObject(response);
                            if (!obj2.getBoolean("error")) {
                                //if no error in response

                                jsonArraygrouplist = obj2.getJSONArray("grouplist");

                                //loop through all grouplist json

                                for(int i = 0; i < jsonArraygrouplist.length(); i++){
                                    JSONObject c = jsonArraygrouplist.getJSONObject(i);


//                                $grouplist["contactid"] = $row["contactid"];
//                                $grouplist["firstname"] = $row["firstname"];
//                                $grouplist["lastname"] = $row["lastname"];
//                                $grouplist["phonenumber"] = $row["phonenumber"];
//                                $grouplist["groupid"] = $row["groupid"];
//                                $grouplist["middlename"] = $row["middlename"];
                                    //(String firstName, String lastName, String middleName, @NonNull String phoneNumber,@NonNull int groupid,int remotegroupid)
                                    restoredGroupList.add(new Grouplist(c.getString("firstname"),c.getString("lastname"),c.getString("middlename"),c.getString("phonenumber"),id1,c.getInt("groupid")));

                                }

                                for (Grouplist grouplist : restoredGroupList){
                                    boolean doesContactExist = groupListViewModel.doesContactExist(grouplist.getPhoneNumber(),grouplist.getGroupid());
                                    if(doesContactExist == true){
                                        Toast.makeText(getContext(), "contact exists", Toast.LENGTH_SHORT).show();

                                    }else{
                                        groupListViewModel.createGroupList(grouplist.getFirstName(),grouplist.getLastName(),grouplist.getMiddleName(),grouplist.getPhoneNumber(),grouplist.getGroupid(),grouplist.getRemotegroupid());
                                    }
                                }
                                Toast.makeText(getContext(), obj2.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                //JSONObject userJson = obj2.getJSONObject("user");
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
                //''messagecontent','groupid'
                Map<String,String> params = new HashMap<>();
                params.put("groupid",remoteId1+"");

                return params;
            }

        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void restoreContactGroup() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETALLCONTACTGROUPS,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        Log.i("response",response);
                        //converting response to json object
                        JSONObject obj2 = new JSONObject(response);
                        //if no error in response
                        if (!obj2.getBoolean("error")) {

                            jsonArraycontactGroups = obj2.getJSONArray("contactgroup");

                            //loop through all contactgroups json

                            for(int i = 0; i < jsonArraycontactGroups.length(); i++){
                                JSONObject c = jsonArraycontactGroups.getJSONObject(i);

//                            $contactgroup["id"] = $row["id"];
//                            $contactgroup["groupname"] = $row["groupname"];
//                            $contactgroup["numofcontacts"] = $row["numofcontacts"];
//                            $contactgroup["userid"] = $row["userid"];
                                //public ContactGroup(String groupname,String numofcontacts, Integer remoteId)
                                restoredContactGroup.add(new ContactGroup(c.getString("groupname"),c.getString("numofcontacts"),c.getInt("id")));
                            }

                            for (ContactGroup contactGroup : restoredContactGroup){
                                boolean doesContactExist = contactGroupViewModel.getContactGroupByName(contactGroup.getGroupname());
                                if(doesContactExist == true){
                                    Toast.makeText(getContext(),"Group Exists",Toast.LENGTH_SHORT).show();
                                }else{
                                    contactGroupViewModel.createGroup(contactGroup.getGroupname(),contactGroup.getNumofcontacts(),contactGroup.getRemoteId());
                                }
                            }
                            Toast.makeText(getContext(), obj2.getString("message"), Toast.LENGTH_SHORT).show();

                            //getting the user from the response
                            //JSONObject userJson = obj2.getJSONObject("user");
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

            //''messagecontent','groupid'
            Map<String,String> params = new HashMap<>();
            params.put("userid",user.getId()+"");

            return params;
        }

    };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }

    private void synContacts() {

        for(ContactGroup contactGroup : contactGroupsForSync){

            syncContactsbyClass(contactGroup);


        }

        for(Grouplist grouplist : groupListViewModel.getGroupListForSync()){
            //Toast.makeText(getContext(),grouplist.getRemotegroupid()+" ",Toast.LENGTH_SHORT).show();
            syncGrouplist(grouplist);
        }

        for(Message message : messageViewModel.getMessageForSync()){
            Toast.makeText(getContext(),message.getRemotegroupid()+" ",Toast.LENGTH_SHORT).show();
            syncMessages(message);

        }

    }

    private void syncMessages(Message message) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_INSERTMESSAGE,
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

                //''messagecontent','groupid'
                Map<String,String> params = new HashMap<>();
                params.put("messagecontent",message.getMessageContent());
                params.put("groupid",message.getRemotegroupid()+"");

                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

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

                                //Get the online group id inorder to
                                //link all the other data items linked to the contact group
                                apiGroupId = userJson.getInt("id");
                                //
                                groupListViewModel.updateGrouplistRemoteid(userJson.getInt("id"),contactGroup.getId());
                                messageViewModel.updateRemoteId(userJson.getInt("id"),contactGroup.getId());
                                Toast.makeText(getContext(), obj.getString("message") + "ID: " + apiGroupId, Toast.LENGTH_SHORT).show();
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

                    //passing the contactgroup class properties
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
