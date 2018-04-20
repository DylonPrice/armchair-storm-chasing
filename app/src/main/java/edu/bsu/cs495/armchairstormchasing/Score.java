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

    public void calculateScore(ArrayList<Folder> folders, int n, GeoPoint point){
        for (int i = 0; i < folders.size(); i++){
            ArrayList<ArrayList<GeoPoint>> polygons = folders.get(i).polygons;
        }
    }

    public boolean isInside(ArrayList<GeoPoint> polygon, int n, GeoPoint currentPoint){
        Double infinity = Double.POSITIVE_INFINITY;
        int count = 0;
        if (n < 3){
            return false;
        }
        GeoPoint extremePoint = new GeoPoint(infinity, currentPoint.getLongitude());

        return true;
    }

}
