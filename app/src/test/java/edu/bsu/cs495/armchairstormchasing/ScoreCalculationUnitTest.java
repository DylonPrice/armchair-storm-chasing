package edu.bsu.cs495.armchairstormchasing;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.osmdroid.util.GeoPoint;

public class ScoreCalculationUnitTest {

    private int currentDayScore = 0;
    private int totalScore = 0;

    @Test
    public void mainTest(){
        ArrayList<Folder> folders = createFolders();
        GeoPoint currentPoint = new GeoPoint(39.50404070558415,-119.794921875);
        calculateScore(folders, currentPoint);
        assertEquals(10, currentDayScore);
        assertEquals(10, totalScore);
    }

    private void calculateScore(ArrayList<Folder> folders, GeoPoint currentPoint){
        int scoreIncrease = 0;
        for (int i = 0; i < folders.size(); i++){
            Folder currentFolder = folders.get(i);
            ArrayList<ArrayList<GeoPoint>> polygons = currentFolder.polygons;
            for (int j = 0; j < polygons.size(); j++){
                ArrayList<GeoPoint> currentPolygon = polygons.get(j);
                if (isInside(currentPolygon, currentPoint)){
                    if (currentFolder.name.equals("Test")){
                        scoreIncrease += 10;
                    }
                }
            }

            currentDayScore += scoreIncrease;
            totalScore += scoreIncrease;
        }
    }

    private ArrayList<Folder> createFolders(){
        ArrayList<Folder> result = new ArrayList<>();
        ArrayList<GeoPoint> polygon = createPolygon();
        ArrayList<ArrayList<GeoPoint>> polygons = new ArrayList<>();
        polygons.add(polygon);
        Folder folder = new Folder("Test", polygons);
        result.add(folder);
        return result;
    }

    private ArrayList<GeoPoint> createPolygon(){
        ArrayList<GeoPoint> result = new ArrayList<>();
        GeoPoint point1 = new GeoPoint(40.313043208880906,-120.91552734375);
        GeoPoint point2 = new GeoPoint(38.53097889440024,-120.8056640625);
        GeoPoint point3 = new GeoPoint(38.702659307238015, -117.9931640625);
        GeoPoint point4 = new GeoPoint(40.430223634508614, -118.6962890625);
        GeoPoint point5 = new GeoPoint(40.313043208880906, -120.91552734375);
        result.add(point1);
        result.add(point2);
        result.add(point3);
        result.add(point4);
        result.add(point5);

        return result;
    }

    private boolean isInside(ArrayList<GeoPoint> polygon, GeoPoint currentPoint){
        boolean result = false;
        int j = polygon.size() - 1;
        for (int i = 0; i < polygon.size(); i++){
            if (polygon.get(i).getLongitude() < currentPoint.getLongitude() && polygon.get(j).getLongitude() >= currentPoint.getLongitude()
                    || polygon.get(j).getLongitude() < currentPoint.getLongitude() && polygon.get(i).getLongitude() >= currentPoint.getLongitude()){
                if ((polygon.get(i).getLatitude() + (currentPoint.getLongitude() - polygon.get(i).getLongitude()) / (polygon.get(j).getLongitude() - polygon.get(i).getLongitude())
                        * (polygon.get(j).getLatitude() - polygon.get(i).getLatitude()) < currentPoint.getLatitude())){
                    result = !result;
                }
            }

            j = i;
        }

        return result;
    }

}
