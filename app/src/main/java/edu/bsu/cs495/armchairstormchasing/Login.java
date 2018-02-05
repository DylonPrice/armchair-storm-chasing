package edu.bsu.cs495.armchairstormchasing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.InputStream;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout profileSection;
    private Button signOutBtn;
    private SignInButton signInBtn;
    private TextView name, email;
    private ImageView profilePicture;
    private GoogleApiClient googleAPiClient;
    private static final int req_code = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        profileSection = findViewById(R.id.prof_section);
        signOutBtn = findViewById(R.id.btn_logout);
        signInBtn = findViewById(R.id.btn_login);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        profilePicture = findViewById(R.id.profile_pic);
        signInBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
        profileSection.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleAPiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_login:
                signIn();
                break;
            case R.id.btn_logout:
                signOut();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void toMapActivity(){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    private void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleAPiClient);
        startActivityForResult(intent, req_code);
        toMapActivity();
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleAPiClient).setResultCallback(new ResultCallback<Status>(){

            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void handleResult(GoogleSignInResult result){
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String userName = account.getDisplayName();
            String userEmail = account.getEmail();
            String imageUrl = account.getPhotoUrl().toString();
            name.setText(userName);
            email.setText(userEmail);
            if (account.getPhotoUrl() != null){
                new LoadProfileImage(profilePicture).execute(imageUrl);
            }
            updateUI(true);
        }
        else {
            updateUI(false);
        }
    }

    private void updateUI(boolean isLoggedIn){
        if (isLoggedIn){
            profileSection.setVisibility(View.VISIBLE);
            signInBtn.setVisibility(View.GONE);
        }
        else {
            profileSection.setVisibility(View.GONE);
            signInBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == req_code){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}

class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
    private ImageView bitmapImage;

    public LoadProfileImage(ImageView bmImage){
        this.bitmapImage = bmImage;
    }

    protected Bitmap doInBackground(String... uri){
        String url = uri[0];
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            icon = BitmapFactory.decodeStream(in);
        }
        catch (Exception e) {
            Log.e("ERROR", e.getMessage());
            e.printStackTrace();
        }
        return icon;
    }

    protected void onPostExecute(Bitmap result){
        if (result != null){
            Bitmap resized = Bitmap.createScaledBitmap(result, 200, 200, true);
            bitmapImage.setImageBitmap(resized);
        }
    }
}
