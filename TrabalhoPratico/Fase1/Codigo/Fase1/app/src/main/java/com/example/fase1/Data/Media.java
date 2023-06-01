package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "media_table")
public class Media {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "media_file")
    private String media_file;

    @ColumnInfo(name = "media_type")
    private String media_type;

    @ColumnInfo(name = "media_pin")
    private int media_pin;


    public Media(int id,String media_file, String media_type, int media_pin) {
        this.id = id;
        this.media_file = media_file;
        this.media_type = media_type;
        this.media_pin = media_pin;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedia_file() {
        return media_file;
    }

    public void setMedia_file(String media_file) {
        this.media_file = media_file;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public int getMedia_pin() {
        return media_pin;
    }

    public void setMedia_pin(int media_pin) {
        this.media_pin = media_pin;
    }


}

