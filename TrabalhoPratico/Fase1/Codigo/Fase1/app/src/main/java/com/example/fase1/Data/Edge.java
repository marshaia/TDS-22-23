package com.example.fase1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "edge_table")
public class Edge {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "edge_start_id")
    private int edge_start_id;

    @ColumnInfo(name = "edge_end_id")
    private int edge_end_id;

    @ColumnInfo(name = "edge_transport")
    private String edge_transport;

    @ColumnInfo(name = "edge_duration")
    private int edge_duration;

    @ColumnInfo(name = "edge_desc")
    private String edge_desc;

    @ColumnInfo(name = "edge_trail")
    private int edge_trail;


    public Edge(int id, int edge_start_id, int edge_end_id, String edge_transport, int edge_duration, String edge_desc, int edge_trail) {
        this.id = id;
        this.edge_start_id = edge_start_id;
        this.edge_end_id = edge_end_id;
        this.edge_transport = edge_transport;
        this.edge_duration = edge_duration;
        this.edge_desc = edge_desc;
        this.edge_trail = edge_trail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEdge_start_id() {
        return edge_start_id;
    }

    public void setEdge_start_id(int edge_start_id) {
        this.edge_start_id = edge_start_id;
    }

    public int getEdge_end_id() {
        return edge_end_id;
    }

    public void setEdge_end_id(int edge_end_id) {
        this.edge_end_id = edge_end_id;
    }

    public String getEdge_transport() {
        return edge_transport;
    }

    public void setEdge_transport(String edge_transport) {
        this.edge_transport = edge_transport;
    }

    public int getEdge_duration() {
        return edge_duration;
    }

    public void setEdge_duration(int edge_duration) {
        this.edge_duration = edge_duration;
    }

    public String getEdge_desc() {
        return edge_desc;
    }

    public void setEdge_desc(String edge_desc) {
        this.edge_desc = edge_desc;
    }

    public int getEdge_trail() {
        return edge_trail;
    }

    public void setEdge_trail(int edge_trail) {
        this.edge_trail = edge_trail;
    }


}
