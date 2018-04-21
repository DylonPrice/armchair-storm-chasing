package edu.bsu.cs495.armchairstormchasing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class End_Of_Day_Screen extends AppCompatActivity {

    Double currentPosLat;
    Double currentPosLong;
    Double totalScore;
    Double dailyScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end__of__day__screen);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentPosLat = extras.getDouble("currentPosLat");
            currentPosLong = extras.getDouble("currentPosLong");
            totalScore = extras.getDouble("totalScore");
            dailyScore = extras.getDouble("dailyScore");
        }
        TextView dayScoreText = findViewById(R.id.txt_dayScoreText);
        dayScoreText.setText(dailyScore.toString());
        TextView totalScoreText = findViewById(R.id.txt_totalScoreText);
        totalScoreText.setText(totalScore.toString());

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
}
