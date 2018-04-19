package edu.bsu.cs495.armchairstormchasing;

import java.util.ArrayList;

public class Folder {
    public final String name;
    public final ArrayList<String> polygons;

    public Folder(String name, ArrayList<String> polygons){
        this.name = name;
        this.polygons = polygons;
    }
}
