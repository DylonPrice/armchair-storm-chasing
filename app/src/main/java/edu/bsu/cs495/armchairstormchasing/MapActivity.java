package edu.bsu.cs495.armchairstormchasing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Timer;
import java.net.URL;
import java.io.File;

import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.backgroundColor;
import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.fontSizeDp;

public class MapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private GoogleApiClient mGoogleApiClient;
    private Handler handler;
    private Runnable runnable;
    GeoPoint currentPos;
    Marker startMarker;
    int currentPointOnRoute;
    int totalPointsOnRoute;
    ArrayList<GeoPoint> routePoints = new ArrayList<>();
    Road road = new Road();
    MapView map;
    boolean isTraveling = false;

    @Override public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_map);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle b = getIntent().getExtras();
        double startLat = b.getDouble("startLat");
        double startLon = b.getDouble("startLon");
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(13.5);
        final GeoPoint startPos = new GeoPoint(startLat, startLon);
        mapController.setCenter(startPos);
        currentPos = startPos;
        startMarker = new Marker(map);
        startMarker.setPosition(startPos);
        startMarker.setTextLabelBackgroundColor(backgroundColor);
        startMarker.setTextLabelFontSize(fontSizeDp);
        startMarker.setIcon(null);
        map.getOverlays().add(startMarker);

        final RoadManager roadManager = new OSRMRoadManager(this);
        final ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        final Polyline roadOverlay = new Polyline();

        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if (isTraveling == false) {
                    updateRoute(waypoints, roadManager, currentPos, p, map, roadOverlay, road, startMarker);
                }
                if (isTraveling == true) {
                    showTravelText();
                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        final Handler timeHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (isTimeBetweenAllowedTime() == false){
                        Intent endOfDayIntent = new Intent(MapActivity.this, End_Of_Day_Screen.class);
                        Bundle endOfDayBundle = new Bundle();
                        endOfDayBundle.putDouble("currentPosLat", currentPos.getLatitude());
                        endOfDayBundle.putDouble("currentPosLong", currentPos.getLongitude());
                        endOfDayBundle.putInt("totalScore", 0);
                        endOfDayBundle.putInt("dailyScore", 0);
                        endOfDayIntent.putExtras(endOfDayBundle);
                        startActivity(endOfDayIntent);
                    };
                    timeHandler.postDelayed(this, 30000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        timeHandler.postDelayed(runnable, 30000);

        setUpNavDrawer();

        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getBaseContext(), mReceive);
        map.getOverlays().add(OverlayEvents);

    }

    public void showTravelText(){
        Toast.makeText(this, "Traveling", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    private void setUpNavDrawer() {
         mDrawerLayout = (DrawerLayout) findViewById(R.id.mapNavDrawer);
         mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
         mDrawerLayout.addDrawerListener(mToggle);
         mToggle.syncState();
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
         navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateRoute(ArrayList<GeoPoint> waypoints, RoadManager roadManager, GeoPoint currentPos,GeoPoint p,MapView map,Polyline roadOverlay, Road road, Marker startMarker){
        waypoints.clear();
        waypoints.add(currentPos);
        waypoints.add(p);
        road = roadManager.getRoad(waypoints);
        roadOverlay.setPoints(road.mRouteHigh);
        roadOverlay.setColor(Color.BLUE);
        startMarker.setTitle(road.getLengthDurationText(this, -1));
        map.getOverlays().add(roadOverlay);
        map.postInvalidate();

        showTravelDialog(road);
    }

    public void showTravelDialog(Road road){
        final Road newRoad = road;
        final AlertDialog travelDialog = new AlertDialog.Builder(MapActivity.this).create();
        travelDialog.setTitle("Begin Travel?");
        travelDialog.setMessage("Would you like to travel to this destination?");
        travelDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        isTraveling = true;
                        updateCurrentLocation(newRoad);
                    }
                });
        travelDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        Handler dialogHandler = new Handler();
        dialogHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                travelDialog.show();
                map.setEnabled(false);
            }
        }, 500);
    }

    public void updateCurrentLocation(Road road){
        totalPointsOnRoute = 0;
        currentPointOnRoute = 0;
        routePoints.clear();
        for (int i = 0; i < road.mRouteHigh.size(); i++){
            routePoints.add(road.mRouteHigh.get(i));
            totalPointsOnRoute+=1;
        }

        final double delay = (road.mDuration/routePoints.size()) * 1000;
        View fadeBackground = findViewById(R.id.fadeBackground);
        fadeBackground.setVisibility(View.VISIBLE);
        fadeBackground.animate().alpha(0.5f);

        handler = new Handler();
        runnable = new Runnable(){
            @Override
                public void run() {
                    updateMarker();
                    handler.postDelayed(this, Double.valueOf(delay).longValue());
        }
    };

        handler.postDelayed(runnable, Double.valueOf(delay).longValue());
    }

    private void updateMarker(){
        try{
            currentPos = routePoints.get(currentPointOnRoute);
            startMarker.setPosition(currentPos);
            startMarker.setTextLabelBackgroundColor(backgroundColor);
            startMarker.setTextLabelFontSize(fontSizeDp);
            startMarker.setIcon(null);
            map.getOverlays().add(startMarker);
            map.postInvalidate();
            currentPointOnRoute+=1;
        }
        catch (IndexOutOfBoundsException e){
            Toast.makeText(this, "You have arrived", Toast.LENGTH_SHORT).show();
            isTraveling = false;
            removeFade();

        }

    }
    private void removeFade(){
        View fadeBackground = findViewById(R.id.fadeBackground);
                    fadeBackground.setVisibility(View.GONE);
            fadeBackground.animate().alpha(0.5f);
    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    @Override
    public void onBackPressed() {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.Logout){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                        }
                    }
            );
        }
  
        if(id == R.id.changeStartingLocation){
            Intent intent = new Intent(this, CityMenuActivity.class);
            startActivity(intent);
        }

        if(id == R.id.stopTravel){
            Toast.makeText(this, "Travel Stopped", Toast.LENGTH_SHORT).show();
            removeFade();
            isTraveling = false;
            handler.removeCallbacks(runnable);
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.mapNavDrawer);
            mDrawerLayout.closeDrawers();
        }
        return false;
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

}