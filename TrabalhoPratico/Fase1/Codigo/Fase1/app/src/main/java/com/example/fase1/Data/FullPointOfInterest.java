package com.example.fase1.Data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class FullPointOfInterest implements Serializable {

    @Embedded
    private PointOfInterest point;

    @Relation(
            parentColumn = "id",
            entityColumn = "media_pin"
    )
    public transient List<Media> medias;

    @Relation(
            parentColumn = "id",
            entityColumn = "point_id"
    )
    public transient List<InfoPoint> infos;


    public PointOfInterest getPoint() {
        return point;
    }

    public void setPoint(PointOfInterest point) {
        this.point = point;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public void setInfos (List<InfoPoint> info) {
        this.infos = info;
    }

    public List<InfoPoint> getInfo () {
        return this.infos;
    }

}
