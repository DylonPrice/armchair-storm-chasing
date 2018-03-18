package edu.bsu.cs495.armchairstormchasing;

import android.content.Context;
import android.os.DropBoxManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
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
import java.util.ArrayList;

import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.backgroundColor;
import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.fontSizeDp;

public class MapActivity extends AppCompatActivity {

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
        final GeoPoint startPoint = new GeoPoint(37.0902, -95.7129);
        mapController.setCenter(startPoint);
        final Road road = new Road();

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
                updateRoute(waypoints,roadManager,startPoint,p,map, roadOverlay, road, startMarker);
                return false;
            }


            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };




        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getBaseContext(), mReceive);
        map.getOverlays().add(OverlayEvents);

    }
    public void updateRoute(ArrayList<GeoPoint> waypoints, RoadManager roadManager, GeoPoint currentPos,GeoPoint p,MapView map,Polyline roadOverlay, Road road, Marker startMarker){
        waypoints.clear();
        map.getOverlays().remove(roadOverlay);
        waypoints.add(currentPos);
        waypoints.add(p);
        road = roadManager.getRoad(waypoints);
        roadOverlay = roadManager.buildRoadOverlay(road);
        updateDurationText(road, startMarker);
        System.out.println(roadOverlay.getPoints());
        map.getOverlays().add(roadOverlay);
    }

    public void updateDurationText(Road road, Marker startMarker){
        Double totalSeconds = road.mDuration;
        Double hours = Math.floor(totalSeconds/3600);
        Double minutes = Math.floor((totalSeconds % 3600)/60);
        startMarker.setTitle(Double.toString(hours) + " h " + Double.toString(minutes) + " min");
    }


    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }
}
