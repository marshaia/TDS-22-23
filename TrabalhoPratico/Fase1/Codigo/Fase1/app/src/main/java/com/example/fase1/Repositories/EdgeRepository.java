package com.example.fase1.Repositories;

import android.app.Application;

import com.example.fase1.DAO.EdgeDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.Edge;


public class EdgeRepository {

    private final EdgeDAO edgeDAO;

    public EdgeRepository(Application application) {
        RoomDB db = RoomDB.getDatabase(application);
        edgeDAO = db.edgeDAO();
    }

    public void insert(Edge edge) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            edgeDAO.insert(edge);
        });
    }
}