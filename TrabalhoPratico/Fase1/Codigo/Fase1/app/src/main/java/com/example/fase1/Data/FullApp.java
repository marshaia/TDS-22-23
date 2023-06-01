package com.example.fase1.Data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FullApp {

    @Embedded
    private App app;

    @Relation(
            parentColumn = "id",
            entityColumn = "app_id"
    )
    public List<Social> socials;

    @Relation(
            parentColumn = "id",
            entityColumn = "app_id"
    )
    public List<Partner> partners;

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public List<Social> getSocials() {
        return socials;
    }

    public void setSocials(List<Social> socials) {
        this.socials = socials;
    }

    public List<Partner> getPartners() {
        return partners;
    }

    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }
}
