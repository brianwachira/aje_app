package com.example.ex_contactapp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ex_contactapp.data.User;
import com.example.ex_contactapp.utils.SaveSharedPreferences;
import com.example.ex_contactapp.utils.SharedPreferenceManager;
import com.example.ex_contactapp.volley.URLs;
import com.example.ex_contactapp.volley.VolleySingleton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.tasks.Task;
import com.jetradarmobile.sociallogin.SocialLogin;
import com.jetradarmobile.sociallogin.SocialLoginCallback;
import com.jetradarmobile.sociallogin.SocialLoginError;
import com.jetradarmobile.sociallogin.SocialNetwork;
import com.jetradarmobile.sociallogin.SocialToken;
import com.jetradarmobile.sociallogin.facebook.FacebookNetwork;
import com.jetradarmobile.sociallogin.google.GoogleNetwork;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, SocialLoginCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private TextView info;
    ImageButton facebookLoginButton;
    ImageButton googleButton;
    TextView skipButton;

    private GoogleApiClient googleApiClient;

    private static final int RC_SIGN_IN = 0;

    private static final int SIGNED_IN = 0;
    private static final int STATE_SIGNING_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN2 = 0;

    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;

    Button googleButtonSignIn;
    GoogleSignInClient googleSignInClient;



    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        info = findViewById(R.id.info);


        googleButton = findViewById(R.id.googleButton);

        skipButton = findViewById(R.id.skipButton);

        skipButton.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        googleButton.setOnClickListener(this);

        googleSignInClient = GoogleSignIn.getClient(this,gso);


        //Check if user is already logged in
//        if(SaveSharedPreferences.getLoggedStatus(getApplicationContext())){
//
//            Intent mainActivityIntent = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(mainActivityIntent);
//
//        }

        if(SharedPreferenceManager.getInstance(this).isLoggedIn()){

            Intent mainActivityIntent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(mainActivityIntent);

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.googleButton:
                    LoginActivity.this.signIn();
                    break;
            case R.id.skipButton:

                Intent mainActivityIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainActivityIntent);

        }

    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(getApplicationContext(),connectionResult.getErrorMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(getApplicationContext(),"Welcome " + account.getGivenName(),Toast.LENGTH_LONG).show();

//            SaveSharedPreferences.setId(getApplicationContext(),account.getId());
//            SaveSharedPreferences.setName(getApplicationContext(),account.getGivenName());
//            SaveSharedPreferences.setLoggedIn(getApplicationContext(),true);
            login(account.getId(),account.getGivenName());

        }catch (ApiException e){

            Log.w("Google Sign In Error","signInResult:failed code="+ e.getStatusCode());
            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();

        }
    }

    private void login(String id, String givenName) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("response",response);
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("givenName"),
                                        userJson.getString("userId")
                                );
                                //storing the user in shared preferences
                                SharedPreferenceManager.getInstance(getApplicationContext()).userLogin(user);

                                //start the main activity
                                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(mainActivityIntent);
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    }){
                    @Override
                    protected Map<String,String> getParams()throws AuthFailureError{
                        Map<String,String> params = new HashMap<>();
                        params.put("userId",id);
                        params.put("givenName",givenName);

                        return params;
                        }
                    };
                    VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
                }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
