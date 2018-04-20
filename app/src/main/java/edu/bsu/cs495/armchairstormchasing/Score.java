package edu.bsu.cs495.armchairstormchasing;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Score {
    private int currentDayScore;
    private int totalScore;

    public Score(int currentDayScore, int totalScore) {
        this.currentDayScore = currentDayScore;
        this.totalScore = totalScore;
    }

    public int getCurrentDayScore() {
        return currentDayScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void calculateScore(ArrayList<Folder> folders, GeoPoint currentPoint){
        for (int i = 0; i < folders.size(); i++){
            Folder currentFolder = folders.get(i);
            ArrayList<ArrayList<GeoPoint>> polygons = currentFolder.polygons;
            for (int j = 0; j < polygons.size(); j++){
                ArrayList<GeoPoint> currentPolygon = polygons.get(i);
                if (isInside(currentPolygon, currentPoint)){
                    if (currentFolder.name.equals("0")){
                        currentDayScore += 1;
                    }
                    if (currentFolder.name.equals("1")){
                        currentDayScore += 2;
                    }
                    if (currentFolder.name.equals("2")){
                        currentDayScore += 3;
                    }
                }
            }
        }
    }

    public boolean isInside(ArrayList<GeoPoint> polygon, GeoPoint currentPoint){
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
