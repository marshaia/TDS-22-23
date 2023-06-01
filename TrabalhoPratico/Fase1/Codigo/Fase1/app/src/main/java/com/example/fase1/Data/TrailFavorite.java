package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trail_favorite_table")
public class TrailFavorite {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "trail_id")
    private int trail_id;

    @ColumnInfo(name = "isPremium")
    private boolean isPremium;

    public TrailFavorite(int trail_id, boolean isPremium) {
        this.trail_id = trail_id;
        this.isPremium = isPremium;
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

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
