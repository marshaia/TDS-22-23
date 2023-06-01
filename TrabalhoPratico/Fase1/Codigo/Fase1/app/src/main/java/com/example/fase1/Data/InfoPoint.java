package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "info_point_table")
public class InfoPoint {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "value")
    private String value;

    @ColumnInfo(name = "attrib")
    private String attrib;

    @ColumnInfo(name = "point_id")
    private int point_id;

    public InfoPoint(int id, String value, String attrib, int point_id) {
        this.id = id;
        this.value = value;
        this.attrib = attrib;
        this.point_id = point_id;
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

    public int getPoint_id() {
        return point_id;
    }

    public void setPoint_id(int point_id) {
        this.point_id = point_id;
    }
}
