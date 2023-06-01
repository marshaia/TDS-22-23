package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "points_table")
public class PointOfInterest implements Serializable {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "pin_name")
    private String pin_name;

    @ColumnInfo(name = "pin_desc")
    private String pin_desc;

    @ColumnInfo(name = "pin_lat")
    private double pin_lat;

    @ColumnInfo(name = "pin_lng")
    private double pin_lng;

    @ColumnInfo(name = "pin_alt")
    private double pin_alt;

    @ColumnInfo(name = "thumbnailURL")
    private String thumbnailURL;


    public PointOfInterest(int id, String pin_name, String pin_desc, double pin_lat, double pin_lng, double pin_alt) {
        this.id = id;
        this.pin_name = pin_name;
        this.pin_desc = pin_desc;
        this.pin_lat = pin_lat;
        this.pin_lng = pin_lng;
        this.pin_alt = pin_alt;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPin_name() {
        return pin_name;
    }

    public void setPin_name(String pin_name) {
        this.pin_name = pin_name;
    }

    public String getPin_desc() {
        return pin_desc;
    }

    public void setPin_desc(String pin_desc) {
        this.pin_desc = pin_desc;
    }

    public double getPin_lat() {
        return pin_lat;
    }

    public void setPin_lat(double pin_lat) {
        this.pin_lat = pin_lat;
    }

    public double getPin_lng() {
        return pin_lng;
    }

    public void setPin_lng(double pin_lng) {
        this.pin_lng = pin_lng;
    }

    public double getPin_alt() {
        return pin_alt;
    }

    public void setPin_alt(double pin_alt) {
        this.pin_alt = pin_alt;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

}
