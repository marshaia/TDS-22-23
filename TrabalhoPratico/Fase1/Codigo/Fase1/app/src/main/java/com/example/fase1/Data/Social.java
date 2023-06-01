package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "social")
public class Social {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "app_id")
    private int app_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "link")
    private String link;

    public Social(int id, int app_id, String name, String url, String link) {
        this.id = id;
        this.app_id = app_id;
        this.name = name;
        this.url = url;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
