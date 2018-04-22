package edu.bsu.cs495.armchairstormchasing;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class End_Of_Day_Screen extends AppCompatActivity {

    Double currentPosLat;
    Double currentPosLong;
    int totalScore;
    int dailyScore;
    int today;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalDateTime current = LocalDateTime.now();
        today = current.getDayOfYear();
        setContentView(R.layout.activity_end__of__day__screen2);
        SharedPreferences saved = getSharedPreferences("ascData", MODE_PRIVATE);
        dailyScore = (saved.getInt("dailyScore",0));
        totalScore = (saved.getInt("totalScore",0));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentPosLat = extras.getDouble("currentPosLat");
            currentPosLong = extras.getDouble("currentPosLong");
            totalScore = extras.getInt("totalScore");
            dailyScore = extras.getInt("dailyScore");
        }
        TextView dayScoreText = (TextView)findViewById(R.id.txt_dayScoreText);
        dayScoreText.setText(String.valueOf(dailyScore));
        TextView totalScoreText = (TextView)findViewById(R.id.txt_totalScoreText);
        totalScoreText.setText(String.valueOf(totalScore));

        try {
            if(isTimeBetweenAllowedTime() == true){
                Button clickNextDayButton = (Button) findViewById(R.id.btn_nextDay);
                clickNextDayButton.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(End_Of_Day_Screen.this, CityMenuActivity.class);
                        startActivity(intent);
                    }
                });

                Button clickNextDaySameButton = (Button) findViewById(R.id.btn_nextDaySame);
                clickNextDaySameButton.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(End_Of_Day_Screen.this, MapActivity.class);
                        Bundle startingPos = new Bundle();
                        startingPos.putDouble("startLat", currentPosLat);
                        startingPos.putDouble("startLon", currentPosLong);
                        intent.putExtras(startingPos);
                        startActivity(intent);
                    }
                });
            }

            else{
                final AlertDialog alertDialog = new AlertDialog.Builder(End_Of_Day_Screen.this).create();
                alertDialog.setTitle("Attention!");
                alertDialog.setMessage("You cannot go to the map at this time. Please wait until the next day.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                alertDialog.show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
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
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("ascData", MODE_PRIVATE).edit();
        editor.putInt("totalScore", totalScore);
        editor.putInt("dailyScore", dailyScore);
        editor.putInt("date", today);
        editor.commit();
    }
}