package edu.bsu.cs495.armchairstormchasing;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_menu);
        list_view = findViewById(R.id.city_list_view);
        //list_view.setAdapter(null);
        TextView textView = new TextView(getBaseContext());
        textView.setText("Select a Starting Location");
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
        cities.add("Olypmia, Washington");
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
                if(position == 0){
                    lat = 32.3668;
                    lon = -86.3000;
                }
                if(position == 1){
                    lat = 58.3019;
                    lon = -134.4197;
                }
                if(position == 2){
                    lat = 33.4484;
                    lon = -112.0740;
                }
                if(position == 3){
                    lat = 34.7465;
                    lon = -92.2896;
                }
                if(position == 4){
                    lat = 38.5816;
                    lon = -121.4944;
                }
                if(position == 5){
                    lat = 39.7392;
                    lon = -104.9903;
                }
                if(position == 6){
                    lat = 41.7658;
                    lon = -72.6734;
                }
                if(position == 7){
                    lat = 39.1582;
                    lon = -75.5244;
                }
                if(position == 8){
                    lat = 30.4383;
                    lon = -84.2807;
                }
                if(position == 9){
                    lat = 33.7490;
                    lon = -84.3880;
                }
                if(position == 10){
                    lat = 21.3069;
                    lon = -157.8583;
                }
                if(position == 11){
                    lat = 43.6187;
                    lon = -116.2146;
                }
                if(position == 12){
                    lat = 39.7817;
                    lon = -89.6501;
                }
                if(position == 13){
                     lat = 39.7684;
                     lon = -86.1581;
                }
                if(position == 14){
                    lat = 41.6005;
                    lon = -93.6091;
                }
                if(position == 15){
                    lat = 39.0558;
                    lon = -95.6890;
                }
                if(position == 16){
                    lat = 38.2009;
                    lon = -84.8733;
                }
                if(position == 17){
                    lat = 30.4515;
                    lon = -91.1871;
                }
                if(position == 18){
                    lat = 44.3106;
                    lon = -69.7795;
                }
                if(position == 19){
                    lat = 38.9784;
                    lon = -76.4922;
                }
                if(position == 20){
                    lat = 42.3601;
                    lon = -71.0589;
                }
                if(position == 21){
                    lat = 42.7325;
                    lon = -84.5555;
                }
                if(position == 22){
                    lat = 44.9537;
                    lon = -93.0900;
                }
                if(position == 23){
                    lat = 32.2988;
                    lon = -90.1848;
                }
                if(position == 24){
                    lat = 38.5767;
                    lon = -92.1735;
                }
                if(position == 25){
                    lat = 46.5891;
                    lon = -112.0391;
                }
                if(position == 26){
                    lat = 40.8136;
                    lon = -96.7026;
                }
                if(position == 27){
                    lat = 39.1638;
                    lon = -119.7674;
                }
                if(position == 28){
                    lat = 43.2081;
                    lon = -71.5376;
                }
                if(position == 29){
                    lat = 40.2206;
                    lon = -74.7597;
                }
                if(position == 30){
                    lat = 35.6870;
                    lon = -105.9378;
                }
                if(position == 31){
                    lat = 42.6526;
                    lon = -73.7562;
                }
                if(position == 32){
                    lat = 35.7796;
                    lon = -78.6382;
                }
                if(position == 33){
                    lat = 46.8083;
                    lon = -100.7837;
                }
                if(position == 34){
                    lat = 39.9612;
                    lon = -82.9988;
                }
                if(position == 35){
                    lat = 35.4676;
                    lon = -97.5164;
                }
                if(position == 36){
                    lat = 44.9429;
                    lon = -123.0351;
                }
                if(position == 37){
                    lat = 40.2732;
                    lon = -76.8867;
                }
                if(position == 38){
                    lat = 41.8240;
                    lon = -71.4128;
                }
                if(position == 39){
                    lat = 34.0007;
                    lon = -81.0348;
                }
                if(position == 40){
                    lat = 44.3683;
                    lon = -100.3510;
                }
                if(position == 41){
                    lat = 36.1627;
                    lon = -86.7816;
                }
                if(position == 42){
                    lat = 30.2672;
                    lon = -97.7431;
                }
                if(position == 43){
                    lat = 40.7608;
                    lon = -111.8910;
                }
                if(position == 44){
                    lat = 44.2601;
                    lon = -72.5754;
                }
                if(position == 45){
                    lat = 37.5407;
                    lon = -77.4360;
                }
                if(position == 46){
                    lat = 47.0379;
                    lon = -122.9007;
                }
                if(position == 47){
                    lat = 38.3498;
                    lon = -81.6326;
                }
                if(position == 48){
                    lat = 43.0731;
                    lon = -89.4012;
                }
                if(position == 49){
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
}
