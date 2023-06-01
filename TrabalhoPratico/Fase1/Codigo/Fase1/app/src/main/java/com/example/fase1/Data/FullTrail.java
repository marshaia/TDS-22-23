package com.example.fase1.Data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FullTrail {

    @Embedded
    private Trail trail;

    @Relation(
            parentColumn = "id",
            entityColumn = "edge_trail"
    )
    public List<Edge> edges;

    @Relation(
            parentColumn = "id",
            entityColumn = "trail_id"
    )
    public List<InfoTrail> infos;


    public Trail getTrail() {
        return trail;
    }

    public void setTrail(Trail trail) {
        this.trail = trail;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public List<InfoTrail> getInfos() {
        return infos;
    }

    public void setInfos(List<InfoTrail> infos) {
        this.infos = infos;
    }
}
