package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table")
public class Contact {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "contact_name")
    private String contact_name;

    @ColumnInfo(name = "contact_phone")
    private int contact_phone;

    @ColumnInfo(name = "contact_url")
    private String contact_url;

    @ColumnInfo(name = "contact_mail")
    private String contact_mail;

    @ColumnInfo(name = "contact_desc")
    private String contact_desc;

    @ColumnInfo(name = "contact_app")
    private String contact_app;


    public Contact(int id, String contact_name, int contact_phone, String contact_url, String contact_mail, String contact_desc, String contact_app) {
        this.id = id;
        this.contact_name = contact_name;
        this.contact_phone = contact_phone;
        this.contact_url = contact_url;
        this.contact_mail = contact_mail;
        this.contact_desc = contact_desc;
        this.contact_app = contact_app;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public int getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(int contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_url() {
        return contact_url;
    }

    public void setContact_url(String contact_url) {
        this.contact_url = contact_url;
    }

    public String getContact_mail() {
        return contact_mail;
    }

    public void setContact_mail(String contact_mail) {
        this.contact_mail = contact_mail;
    }

    public String getContact_desc() {
        return contact_desc;
    }

    public void setContact_desc(String contact_desc) {
        this.contact_desc = contact_desc;
    }

    public String getContact_app() {
        return contact_app;
    }

    public void setContact_app(String contact_app) {
        this.contact_app = contact_app;
    }
}
