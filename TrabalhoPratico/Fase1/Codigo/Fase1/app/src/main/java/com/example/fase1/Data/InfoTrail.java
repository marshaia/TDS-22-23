package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "info_trail_table")
public class InfoTrail {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "value")
    private String value;

    @ColumnInfo(name = "attrib")
    private String attrib;

    @ColumnInfo(name = "trail_id")
    private int trail_id;


    public InfoTrail(int id, String value, String attrib,int trail_id) {
        this.id = id;
        this.value = value;
        this.attrib = attrib;
        this.trail_id = trail_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttrib() {
        return attrib;
    }

    public void setAttrib(String attrib) {
        this.attrib = attrib;
    }

    public int getTrail_id() {
        return trail_id;
    }

    public void setTrail_id(int trail_id) {
        this.trail_id = trail_id;
    }
}
