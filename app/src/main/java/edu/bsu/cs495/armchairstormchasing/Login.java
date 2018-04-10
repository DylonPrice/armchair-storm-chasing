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

import java.io.InputStream;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, IAsyncResponse {

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
        // DOWNLOAD TEST
        testDownload();
        // DOWNLOAD TEST COMPLETE
        setContentView(R.layout.activity_login);
        signInBtn = findViewById(R.id.btn_login);
        signInBtn.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleAPiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    public void testDownload(){
        DownloadDataAsync asyncDownload = new DownloadDataAsync(this);
        asyncDownload.delegate = this;
        String fileUrl = "https://www.weather.gov/source/crh/shapefiles/warnings.kml";
        new DownloadDataAsync(this).execute(fileUrl);
    }

    @Override
    public void onProcessFinish(String output){

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_login:
                signIn();
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
