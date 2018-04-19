package edu.bsu.cs495.armchairstormchasing;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {


    private static final String ns = null;

    public List Parse(InputStream in) throws XmlPullParserException, IOException {
        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally{
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List folders = new ArrayList();
        Boolean isFirstFolder = true;

        parser.require(XmlPullParser.START_TAG, ns, "kml");
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if (name.equals("Folder") && isFirstFolder) {
                parser.nextTag();
                isFirstFolder = false;
                name = parser.getName();
            }
            if (name.equals("Folder") && !isFirstFolder){
                folders.add(readFolder(parser));
            }
            else {
                skip(parser);
            }
        }

        return folders;
    }

    private Folder readFolder(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Folder");
        String name = null;
        String coordinates = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String entryName = parser.getName();
            if (entryName.equals("name")) {
                name = readName(parser);
            }
            else if (entryName.equals("Placemark")){
                coordinates = readPlacemark(parser);
            } else {
                skip(parser);
            }
        }

        return new Folder(name, coordinates);
    }

    private String readPlacemark(XmlPullParser parser) throws XmlPullParserException, IOException {
        String coordinates = null;

        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String entryName = parser.getName();
            if (entryName.equals("MultiGeometry")){
                coordinates = readMultiGeometry(parser);
            }
            else {
                skip(parser);
            }
        }

        return coordinates;
    }

    private String readMultiGeometry(XmlPullParser parser) throws XmlPullParserException, IOException {
        String coordinates = null;

        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String entryName = parser.getName();
            if (entryName.equals("Polygon")){
                coordinates = readPolygon(parser);
            }
            else {
                skip(parser);
            }
        }

        return coordinates;
    }

    private String readPolygon(XmlPullParser parser) throws XmlPullParserException, IOException {
        String coordinates = null;

        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String entryName = parser.getName();
            if (entryName.equals("outerBoundaryIs")){
                coordinates = readOuterBoundary(parser);
            }
            else {
                skip(parser);
            }
        }

        return coordinates;
    }

    private String readOuterBoundary(XmlPullParser parser) throws XmlPullParserException, IOException {
        String coordinates = null;

        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String entryName = parser.getName();
            if (entryName.equals("LinearRing")){
                coordinates = readLinearRing(parser);
            }
            else {
                skip(parser);
            }
        }

        return coordinates;
    }


    private String readLinearRing(XmlPullParser parser) throws XmlPullParserException, IOException {
        String coordinates = null;

        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String entryName = parser.getName();
            if (entryName.equals("coordinates")){
                coordinates = readCoordinates(parser);
            }
            else{
                skip(parser);
            }
        }

        return coordinates;
    }

    private String readCoordinates(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, "coordinates");
        String coordinates = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "coordinates");
        return coordinates;
    }

    private String readName(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG){
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch(parser.next()){
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
