package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "trail_history_table")
public class TrailHistory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "trail_id")
    private int trail_id;

    @ColumnInfo(name = "isPremium")
    private boolean isPremium;

    @ColumnInfo(name = "finished")
    private boolean finished;

    @ColumnInfo(name = "cancelled")
    private boolean cancelled;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "distance")
    private String distance;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "last_update")
    private Date last_update;

    public TrailHistory(int trail_id, boolean isPremium, boolean finished, Date last_update) {
        this.trail_id = trail_id;
        this.isPremium = isPremium;
        this.finished = finished;
        this.cancelled = false;
        this.distance = "----";
        this.time = "----";
        this.last_update = last_update;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrail_id() {
        return trail_id;
    }

    public void setTrail_id(int trail_id) {
        this.trail_id = trail_id;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }

    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }

}
