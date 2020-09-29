package com.example.ex_contactapp;

import android.Manifest;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.jetradarmobile.sociallogin.SocialLogin;
import com.jetradarmobile.sociallogin.SocialLoginCallback;
import com.jetradarmobile.sociallogin.SocialLoginError;
import com.jetradarmobile.sociallogin.SocialNetwork;
import com.jetradarmobile.sociallogin.SocialToken;
import com.jetradarmobile.sociallogin.facebook.FacebookNetwork;
import com.jetradarmobile.sociallogin.google.GoogleNetwork;

import org.jetbrains.annotations.NotNull;


import java.util.Arrays;
import java.util.List;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener, SocialLoginCallback {

    private TextView info;
    ImageButton facebookLoginButton;
    Button googleButton;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        info = findViewById(R.id.info);

        facebookLoginButton = findViewById(R.id.facebookButton);

        googleButton = findViewById(R.id.googleButton);

        facebookLoginButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.facebookButton:
                ImageButton facebookLoginButton= findViewById(R.id.facebookButton);
                    List<String> permissions = Arrays.asList("public_profile");
                    SocialLogin.Factory.getInstance().loginTo(this,new FacebookNetwork(permissions),this);
                    break;
            case R.id.googleButton:
                    SocialLogin.Factory.getInstance().loginTo(this,new GoogleNetwork())
                    break;
        }

    }

    @Override
    public void onLoginError(@NotNull SocialNetwork socialNetwork, @NotNull SocialLoginError socialLoginError) {
        displayError(socialLoginError.getMessage());
    }

    @Override
    public void onLoginSuccess(@NotNull SocialNetwork socialNetwork, @NotNull SocialToken socialToken) {
            displayLoginInfo(socialToken);
    }

    private void displayLoginInfo(SocialToken socialToken){

        String info = "token = " + socialToken.getToken() + "\n\n" +
                "user id = " + socialToken.getUserId() + "\n\n" +
                "user name = " + socialToken.getUserName() + "\n\n";

        Toast.makeText(getApplicationContext(),info,Toast.LENGTH_LONG).show();

        this.info.setText(info);

    }
    private void displayError(String error){
        info.setText(error);
    }
}
