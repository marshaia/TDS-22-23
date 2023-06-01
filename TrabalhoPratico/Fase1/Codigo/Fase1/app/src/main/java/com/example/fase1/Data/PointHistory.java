package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "point_history_table")
public class PointHistory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "point_id")
    private int point_id;

    @ColumnInfo(name = "isPremium")
    private boolean isPremium;

    @ColumnInfo(name = "date")
    private Date date;

    public PointHistory(int point_id, boolean isPremium,  Date date) {
        this.point_id = point_id;
        this.isPremium = isPremium;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoint_id() {
        return point_id;
    }

    public void setPoint_id(int point_id) {
        this.point_id = point_id;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
