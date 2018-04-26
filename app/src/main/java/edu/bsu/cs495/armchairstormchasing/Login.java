package edu.bsu.cs495.armchairstormchasing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private SignInButton signInBtn;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int req_code = 9001;
    boolean validTime;
    int today;

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
        today = current.getDayOfYear();
        try {
            if(!isTimeBetweenAllowedTime()){
                validTime = false;
            }
            else {
                validTime = true;
            }
        } catch (ParseException e) {
            Log.e("Login", "Login error: " + e.toString());
        }

    }
    private boolean isTimeBetweenAllowedTime() throws ParseException {

        LocalTime startTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startTime = LocalTime.of(13, 0);
        }

        LocalTime endTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            endTime = LocalTime.of(23, 0);
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
        SharedPreferences saved = getSharedPreferences("ascData", MODE_PRIVATE);
        float savedLat = (saved.getFloat("currentPositionLat",0));
        float savedLong = (saved.getFloat("currentPositionLong",0));
        int savedDate = (saved.getInt("date",0));
        int totalScore = (saved.getInt("totalScore", 0));
        if (savedLat != 0 && validTime && today == savedDate){
            Intent intent = new Intent(Login.this, MapActivity.class);
            Bundle b = new Bundle();
            b.putDouble("startLat", savedLat);
            b.putDouble("startLon", savedLong);
            intent.putExtras(b);
            startActivity(intent);
        }
        if (savedLat != 0 && validTime && today != savedDate && totalScore != 0){
            Intent intent = new Intent(Login.this, End_Of_Day_Screen.class);
            Bundle b = new Bundle();
            b.putInt("totalScore", totalScore);
            intent.putExtras(b);
            startActivity(intent);
        }
        if (!validTime){
            Intent intent = new Intent(Login.this, End_Of_Day_Screen.class);
            startActivity(intent);
        }
        else {
            toCityMenu();
        }
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
