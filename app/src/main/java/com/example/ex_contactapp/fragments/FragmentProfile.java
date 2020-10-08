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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ex_contactapp.LoginActivity;
import com.example.ex_contactapp.MainActivity;
import com.example.ex_contactapp.R;
import com.example.ex_contactapp.adapters.ContactGroupsRvAdapter;
import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.User;
import com.example.ex_contactapp.utils.SaveSharedPreferences;
import com.example.ex_contactapp.utils.SharedPreferenceManager;
import com.example.ex_contactapp.viewmodels.ContactGroupViewModel;
import com.example.ex_contactapp.volley.URLs;
import com.example.ex_contactapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentProfile extends Fragment {
    private View v;
    String name = " ";
    TextView accountName;

    ContactGroupViewModel contactGroupViewModel;
    Button btnSync;
    List<ContactGroup> contactGroupsForSync;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile,container,false);

         accountName = v.findViewById(R.id.accountName);

        btnSync = v.findViewById(R.id.buttonSync);
        name = SaveSharedPreferences.getGivenName(getContext());
        accountName.setText(name);
        contactGroupsForSync = new ArrayList<>();

        if(SharedPreferenceManager.getInstance(getContext()).isLoggedIn()){
            user = SharedPreferenceManager.getInstance(getContext()).getUser();

            accountName.setText(user.getGivenName());
        }else{
            Intent intent = new Intent(v.getContext(),LoginActivity.class);
            startActivity(intent);

        }

        contactGroupViewModel = ViewModelProviders.of(this, new ContactGroupViewModel.Factory(getActivity().getApplicationContext())).get(ContactGroupViewModel.class);


        for(ContactGroup contactGroup :contactGroupViewModel.readGroupForSync()){

            contactGroupsForSync.add(new ContactGroup(contactGroup.getId(),contactGroup.getGroupname(),contactGroup.getNumofcontacts())) ;
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
                syncContactsbyClass(contactGroup);
        }
    }

    public void syncContactsbyClass(ContactGroup contactGroup){


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
                                Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object

                                ContactGroup contactGroup = new ContactGroup(
//                                        'id'=>$id,
//                                'groupname'=>$groupname,
//                                        'numofcontacts'=>$numofcontacts,
//                                        'userid'=>$userid
                                        userJson.getInt("id"),
                                        userJson.getString("groupname"),
                                        userJson.getString("numofcontacts")
                                );
                                Toast.makeText(getContext(), contactGroup.getGroupname(), Toast.LENGTH_SHORT).show();


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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
