package com.example.fase1.Repositories;

import android.app.Application;


import com.example.fase1.DAO.InfoPointDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.InfoPoint;


public class InfoPointRepository {

    private final InfoPointDAO infoPointDAO;

    public InfoPointRepository(Application application) {
        RoomDB db = RoomDB.getDatabase(application);
        infoPointDAO = db.infoPointDAO();
    }

    public void insert(InfoPoint infoPoint) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            infoPointDAO.insert(infoPoint);
        });
    }
}
