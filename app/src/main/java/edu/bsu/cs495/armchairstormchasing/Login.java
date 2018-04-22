package edu.bsu.cs495.armchairstormchasing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

import org.osmdroid.util.GeoPoint;

import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout profileSection;
    private SignInButton signInBtn;
    private TextView name, email;
    private ImageView profilePicture;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int req_code = 9001;
    boolean validTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInBtn = findViewById(R.id.btn_login);
        signInBtn.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);

        LocalDateTime current = LocalDateTime.now();
        int today = current.getDayOfYear();
        try {
            if(isTimeBetweenAllowedTime() == false){
                validTime = false;
            }
            else {
                validTime = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SharedPreferences saved = getSharedPreferences("ascData", MODE_PRIVATE);
        float savedLat = (saved.getFloat("currentPositionLat",0));
        float savedLong = (saved.getFloat("currentPositionLong",0));
        int savedDate = (saved.getInt("date",0));

        if (savedLat != 0 && validTime == true && today == savedDate){
            Intent intent = new Intent(Login.this, MapActivity.class);
            Bundle b = new Bundle();
            b.putDouble("startLat", savedLat);
            b.putDouble("startLon", savedLong);
            intent.putExtras(b);
            startActivity(intent);
        }
        else if (validTime == false){
            Intent intent = new Intent(Login.this, End_Of_Day_Screen.class);
            startActivity(intent);
        }
        else if(validTime == true && today != savedDate){
            toCityMenu();
        }

    }
    private boolean isTimeBetweenAllowedTime() throws ParseException {

        LocalTime startTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startTime = LocalTime.of(13, 0);
        }

        LocalTime endTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            endTime = LocalTime.of(22, 0);
        }

        LocalTime current = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            current = LocalTime.now();
        }

        boolean isCurrentBetweenStartAndEnd =
                false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            isCurrentBetweenStartAndEnd = current.isAfter(startTime) && current.isBefore(endTime);
        }

        return isCurrentBetweenStartAndEnd;
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

    private void signIn(){
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, req_code);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == req_code) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void updateUI(){
        toCityMenu();
    }

    public void toCityMenu(){
        Intent intent = new Intent(this, CityMenuActivity.class);
        startActivity(intent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI();
        } catch (ApiException e) {
            updateUI();
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
