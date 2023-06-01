package com.example.fase1.Repositories;

import android.app.Application;

import com.example.fase1.DAO.InfoTrailDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.InfoTrail;


public class InfoTrailRepository {

    private final InfoTrailDAO infoTrailDAO;

    public InfoTrailRepository(Application application) {
        RoomDB db = RoomDB.getDatabase(application);
        infoTrailDAO = db.infoTrailDAO();
    }

    public void insert(InfoTrail infoTrail) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            infoTrailDAO.insert(infoTrail);
        });
    }
}
