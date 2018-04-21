package edu.bsu.cs495.armchairstormchasing;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Folder {
    public final String name;
    public final ArrayList<ArrayList<GeoPoint>> polygons;

    public Folder(String name, ArrayList<ArrayList<GeoPoint>> polygons){
        this.name = name;
        this.polygons = polygons;
    }
}