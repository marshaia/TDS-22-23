package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "partner_table")
public class Partner {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "app_id")
    private int app_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "mail")
    private String mail;

    @ColumnInfo(name = "desc")
    private String desc;

    public Partner(int id, int app_id, String name, String phone, String url, String mail, String desc) {
        this.id = id;
        this.app_id = app_id;
        this.name = name;
        this.phone = phone;
        this.url = url;
        this.mail = mail;
        this.desc = desc;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
