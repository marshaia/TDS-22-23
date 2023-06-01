package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "app_table")
public class App {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "app_name")
    private String name;

    @ColumnInfo(name = "desc")
    private String desc;

    @ColumnInfo(name = "app_landing_page_text")
    private String app_landing_page_text;

    public App(int id, String name, String desc, String app_landing_page_text) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.app_landing_page_text = app_landing_page_text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getApp_landing_page_text() {
        return app_landing_page_text;
    }

    public void setApp_landing_page_text(String app_landing_page_text) {
        this.app_landing_page_text = app_landing_page_text;
    }
}
