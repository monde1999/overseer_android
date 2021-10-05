package com.adventurers.overseer.report.models;

import com.adventurers.overseer.map.models.Location;

import java.io.File;
import java.util.List;

public class CreateReportData {
    private int user;
    double latitude;
    double longitude;
    private int id;
    private String description;
    private List<File> images;
    private int floodLevel;

    public CreateReportData(int user, String description, Location location, int floodLevel, List<File> images ){
        this.user = user;
        this.description=description;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.images=images;
        this.floodLevel = floodLevel;
    }
    public int getUser(){
        return user;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude() {return longitude;}
    public int getId(){
        return id;
    }
    public List<File> getImages() {
        return images;
    }
    public  String getDescription(){
        return description;
    }
    public int getFloodLevel(){return floodLevel;}


}