package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trails_table")
public class Trail {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "trail_img")
    private String trail_img;

    @ColumnInfo(name = "trail_name")
    private String trail_name;

    @ColumnInfo(name = "trail_desc")
    private String trail_desc;

    @ColumnInfo(name = "trail_duration")
    private int trail_duration;

    @ColumnInfo(name = "trail_difficulty")
    private String trail_difficulty;

    @ColumnInfo(name = "started")
    private boolean started;

    @ColumnInfo(name = "stopped")
    private boolean stopped;


    public Trail(int id, String trail_img, String trail_name, String trail_desc, int trail_duration, String trail_difficulty) {
        this.id = id;
        this.trail_img = trail_img;
        this.trail_name = trail_name;
        this.trail_desc = trail_desc;
        this.trail_duration = trail_duration;
        this.trail_difficulty = trail_difficulty;
        this.started = false;
        this.stopped = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrail_img() {
        return trail_img;
    }

    public void setTrail_img(String trail_img) {
        this.trail_img = trail_img;
    }

    public String getTrail_name() {
        return trail_name;
    }

    public void setTrail_name(String trail_name) {
        this.trail_name = trail_name;
    }

    public String getTrail_desc() {
        return trail_desc;
    }

    public void setTrail_desc(String trail_desc) {
        this.trail_desc = trail_desc;
    }

    public int getTrail_duration() {
        return trail_duration;
    }

    public void setTrail_duration(int trail_duration) {
        this.trail_duration = trail_duration;
    }

    public String getTrail_difficulty() {
        return trail_difficulty;
    }

    public void setTrail_difficulty(String trail_difficulty) {
        this.trail_difficulty = trail_difficulty;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

}
