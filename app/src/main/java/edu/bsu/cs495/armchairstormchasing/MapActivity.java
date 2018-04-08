package edu.bsu.cs495.armchairstormchasing;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.TextView;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadLeg;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import java.util.ArrayList;
import java.util.Timer;
import java.net.URL;
import java.io.File;

import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.backgroundColor;
import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.fontSizeDp;

public class MapActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_map);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        final MapView map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(5);
        final GeoPoint currentPos = new GeoPoint(37.0902, -95.7129);
        mapController.setCenter(currentPos);
        final Road road = new Road();
        final Timer timer = new Timer();

        final Marker startMarker = new Marker(map);
        startMarker.setPosition(new GeoPoint(38.0, -95.0));
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
                TextView latLong = findViewById(R.id.latLong);
                latLong.setText(p.getLatitude() + " , " + p.getLongitude());
                startMarker.setPosition(p);
                updateRoute(waypoints,roadManager,currentPos,p,map, roadOverlay, road, startMarker);
                return false;
            }


            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };


        setUpNavDrawer();

        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getBaseContext(), mReceive);
        map.getOverlays().add(OverlayEvents);

    }

    private void setUpNavDrawer() {
         mDrawerLayout = (DrawerLayout) findViewById(R.id.mapNavDrawer);
         mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
         mDrawerLayout.addDrawerListener(mToggle);
         mToggle.syncState();
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        map.getOverlays().remove(roadOverlay);
        waypoints.add(currentPos);
        waypoints.add(p);
        road = roadManager.getRoad(waypoints);
        roadOverlay = roadManager.buildRoadOverlay(road);
        startMarker.setTitle(road.getLengthDurationText(this,-1));
        System.out.println();
        System.out.println(roadOverlay.getPoints());
        map.getOverlays().add(roadOverlay);
    }

    public void updateCurrentLocation(Road road, GeoPoint currentPos){
        for (int i = 0; i < road.mLegs.size(); i++){
            RoadLeg currentLeg = road.mLegs.get(i);
            double legTime = currentLeg.mDuration;
            double legLength = currentLeg.mLength;
            double kps = legLength/legTime;

        }

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
}
