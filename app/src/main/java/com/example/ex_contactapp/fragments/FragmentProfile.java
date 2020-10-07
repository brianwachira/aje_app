package com.example.ex_contactapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ex_contactapp.LoginActivity;
import com.example.ex_contactapp.MainActivity;
import com.example.ex_contactapp.R;
import com.example.ex_contactapp.data.User;
import com.example.ex_contactapp.utils.SaveSharedPreferences;
import com.example.ex_contactapp.utils.SharedPreferenceManager;


public class FragmentProfile extends Fragment {
    private View v;
    String name = " ";
    TextView accountName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile,container,false);

         accountName = v.findViewById(R.id.accountName);


        name = SaveSharedPreferences.getGivenName(getContext());
        accountName.setText(name);

        if(SharedPreferenceManager.getInstance(getContext()).isLoggedIn()){
            User user = SharedPreferenceManager.getInstance(getContext()).getUser();

            accountName.setText(user.getGivenName());
        }else{
            Intent intent = new Intent(v.getContext(),LoginActivity.class);
            startActivity(intent);

        }
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
