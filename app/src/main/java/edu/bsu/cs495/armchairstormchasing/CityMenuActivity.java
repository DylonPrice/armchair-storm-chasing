package edu.bsu.cs495.armchairstormchasing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CityMenuActivity extends Activity{

    ListView list_view;
    List<String> cities = new ArrayList<>();
    ArrayAdapter<String> adapter;
    int totalScore = 0;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_menu);
        list_view = findViewById(R.id.city_list_view);
        TextView textView = new TextView(getBaseContext());
        textView.setText("Select a Starting Location");
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        list_view.addHeaderView(textView);
        cities.add("Montgomery, Alabama");
        cities.add("Juneau, Alaska");
        cities.add("Phoenix, Arizona");
        cities.add("Little Rock, Arkansas");
        cities.add("Sacramento, California");
        cities.add("Denver, Colorado");
        cities.add("Hartford, Connecticut");
        cities.add("Dover, Delaware");
        cities.add("Tallahassee, Florida");
        cities.add("Atlanta, Georgia");
        cities.add("Honolulu, Hawaii");
        cities.add("Boise, Idaho");
        cities.add("Springfield, Illinois");
        cities.add("Indianapolis, Indiana");
        cities.add("Des Moines, Iowa");
        cities.add("Topeka, Kansas");
        cities.add("Frankfort, Kentucky");
        cities.add("Baton Rouge, Louisiana");
        cities.add("Augusta, Maine");
        cities.add("Annapolis, Maryland");
        cities.add("Boston, Massachusetts");
        cities.add("Lansing, Michigan");
        cities.add("St. Paul, Minnesota");
        cities.add("Jackson, Mississippi");
        cities.add("Jefferson City, Missouri");
        cities.add("Helena, Montana");
        cities.add("Lincoln, Nebraska");
        cities.add("Carson City, Nevada");
        cities.add("Concord, New Hampshire");
        cities.add("Trenton, New Jersey");
        cities.add("Santa Fe, New Mexico");
        cities.add("Albany, New York");
        cities.add("Raleigh, North Carolina");
        cities.add("Bismarck, North Dakota");
        cities.add("Columbus, Ohio");
        cities.add("Oklahoma City, Oklahoma");
        cities.add("Salem, Oregon");
        cities.add("Harrisburg, Pennsylvania");
        cities.add("Providence, Rhode Island");
        cities.add("Columbia, South Carolina");
        cities.add("Pierre, South Dakota");
        cities.add("Nashville, Tennessee");
        cities.add("Austin, Texas");
        cities.add("Salt Lake City, Utah");
        cities.add("Montpelier, Vermont");
        cities.add("Richmond, Virginia");
        cities.add("Olympia, Washington");
        cities.add("Charleston, West Virginia");
        cities.add("Madison, Wisconsin");
        cities.add("Cheyenne, Wyoming");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, cities);
        list_view.setAdapter(adapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                double lat = 0.0;
                double lon = 0.0;
                Object o  = cities.get(position - 1);
                String cityName = o.toString();
                System.out.println(cityName);
                if(cityName.equals("Montgomery, Alabama")){
                    lat = 32.3668;
                    lon = -86.3000;
                }
                if(cityName.equals("Juneau, Alaska")){
                    lat = 58.3019;
                    lon = -134.4197;
                }
                if(cityName.equals("Phoenix, Arizona")){
                    lat = 33.4484;
                    lon = -112.0740;
                }
                if(cityName.equals("Little Rock, Arkansas")){
                    lat = 34.7465;
                    lon = -92.2896;
                }
                if(cityName.equals("Sacramento, California")){
                    lat = 38.5816;
                    lon = -121.4944;
                }
                if(cityName.equals("Denver, Colorado")){
                    lat = 39.7392;
                    lon = -104.9903;
                }
                if(cityName.equals("Hartford, Connecticut")){
                    lat = 41.7658;
                    lon = -72.6734;
                }
                if(cityName.equals("Dover, Delaware")){
                    lat = 39.1582;
                    lon = -75.5244;
                }
                if(cityName.equals("Tallahassee, Florida")){
                    lat = 30.4383;
                    lon = -84.2807;
                }
                if(cityName.equals("Atlanta, Georgia")){
                    lat = 33.7490;
                    lon = -84.3880;
                }
                if(cityName.equals("Honolulu, Hawaii")){
                    lat = 21.3069;
                    lon = -157.8583;
                }
                if(cityName.equals("Boise, Idaho")){
                    lat = 43.6187;
                    lon = -116.2146;
                }
                if(cityName.equals("Springfield, Illinois")){
                    lat = 39.7817;
                    lon = -89.6501;
                }
                if(cityName.equals("Indianapolis, Indiana")){
                     lat = 39.7684;
                     lon = -86.1581;
                }
                if(cityName.equals("Des Moines, Iowa")){
                    lat = 41.6005;
                    lon = -93.6091;
                }
                if(cityName.equals("Topeka, Kansas")){
                    lat = 39.0558;
                    lon = -95.6890;
                }
                if(cityName.equals("Frankfort, Kentucky")){
                    lat = 38.2009;
                    lon = -84.8733;
                }
                if(cityName.equals("Baton Rouge, Louisiana")){
                    lat = 30.4515;
                    lon = -91.1871;
                }
                if(cityName.equals("Augusta, Maine")){
                    lat = 44.3106;
                    lon = -69.7795;
                }
                if(cityName.equals("Annapolis, Maryland")){
                    lat = 38.9784;
                    lon = -76.4922;
                }
                if(cityName.equals("Boston, Massachusetts")){
                    lat = 42.3601;
                    lon = -71.0589;
                }
                if(cityName.equals("Lansing, Michigan")){
                    lat = 42.7325;
                    lon = -84.5555;
                }
                if(cityName.equals("St. Paul, Minnesota")){
                    lat = 44.9537;
                    lon = -93.0900;
                }
                if(cityName.equals("Jackson, Mississippi")){
                    lat = 32.2988;
                    lon = -90.1848;
                }
                if(cityName.equals("Jefferson City, Missouri")){
                    lat = 38.5767;
                    lon = -92.1735;
                }
                if(cityName.equals("Helena, Montana")){
                    lat = 46.5891;
                    lon = -112.0391;
                }
                if(cityName.equals("Lincoln, Nebraska")){
                    lat = 40.8136;
                    lon = -96.7026;
                }
                if(cityName.equals("Carson City, Nevada")){
                    lat = 39.1638;
                    lon = -119.7674;
                }
                if(cityName.equals("Concord, New Hampshire")){
                    lat = 43.2081;
                    lon = -71.5376;
                }
                if(cityName.equals("Trenton, New Jersey")){
                    lat = 40.2206;
                    lon = -74.7597;
                }
                if(cityName.equals("Santa Fe, New Mexico")){
                    lat = 35.6870;
                    lon = -105.9378;
                }
                if(cityName.equals("Albany, New York")){
                    lat = 42.6526;
                    lon = -73.7562;
                }
                if(cityName.equals("Raleigh, North Carolina")){
                    lat = 35.7796;
                    lon = -78.6382;
                }
                if(cityName.equals("Bismarck, North Dakota")){
                    lat = 46.8083;
                    lon = -100.7837;
                }
                if(cityName.equals("Columbus, Ohio")){
                    lat = 39.9612;
                    lon = -82.9988;
                }
                if(cityName.equals("Oklahoma City, Oklahoma")){
                    lat = 35.4676;
                    lon = -97.5164;
                }
                if(cityName.equals("Salem, Oregon")){
                    lat = 44.9429;
                    lon = -123.0351;
                }
                if(cityName.equals("Harrisburg, Pennsylvania")){
                    lat = 40.2732;
                    lon = -76.8867;
                }
                if(cityName.equals("Providence, Rhode Island")){
                    lat = 41.8240;
                    lon = -71.4128;
                }
                if(cityName.equals("Columbia, South Carolina")){
                    lat = 34.0007;
                    lon = -81.0348;
                }
                if(cityName.equals("Pierre, South Dakota")){
                    lat = 44.3683;
                    lon = -100.3510;
                }
                if(cityName.equals("Nashville, Tennessee")){
                    lat = 36.1627;
                    lon = -86.7816;
                }
                if(cityName.equals("Austin, Texas")){
                    lat = 30.2672;
                    lon = -97.7431;
                }
                if(cityName.equals("Salt Lake City, Utah")){
                    lat = 40.7608;
                    lon = -111.8910;
                }
                if(cityName.equals("Montpelier, Vermont")){
                    lat = 44.2601;
                    lon = -72.5754;
                }
                if(cityName.equals("Richmond, Virginia")){
                    lat = 37.5407;
                    lon = -77.4360;
                }
                if(cityName.equals("Olympia, Washington")){
                    lat = 47.0379;
                    lon = -122.9007;
                }
                if(cityName.equals("Charleston, West Virginia")){
                    lat = 38.3498;
                    lon = -81.6326;
                }
                if(cityName.equals("Madison, Wisconsin")){
                    lat = 43.0731;
                    lon = -89.4012;
                }
                if(cityName.equals("Cheyenne, Wyoming")){
                    lat = 41.1400;
                    lon = -104.8202;
                }
                Intent intent = new Intent(CityMenuActivity.this, MapActivity.class);
                Bundle b = new Bundle();
                b.putDouble("startLat", lat);
                b.putDouble("startLon", lon);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("ascData", MODE_PRIVATE).edit();
        editor.putInt("totalScore", totalScore);
        editor.commit();
    }
}

