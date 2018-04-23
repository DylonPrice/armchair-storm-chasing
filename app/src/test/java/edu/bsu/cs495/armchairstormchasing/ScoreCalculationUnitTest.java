package edu.bsu.cs495.armchairstormchasing;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.osmdroid.util.GeoPoint;

/**
 * Created by DylonPrice on 4/22/18.
 */

public class ScoreCalculationUnitTest {

    int currentDayScore = 0;
    int totalScore = 0;

    @Test
    public void mainTest(){
        ArrayList<Folder> folders = new ArrayList<>();
        GeoPoint currentPoint = new GeoPoint(0,0);
        calculateScore(folders, currentPoint);
        assertEquals(0, currentDayScore);
        assertEquals(0, totalScore);
    }

    public void calculateScore(ArrayList<Folder> folders, GeoPoint currentPoint){

    }

}
