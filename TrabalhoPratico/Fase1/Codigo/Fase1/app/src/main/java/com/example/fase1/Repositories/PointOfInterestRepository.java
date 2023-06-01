package com.example.fase1.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fase1.DAO.PointOfInterestDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.Media;
import com.example.fase1.Data.PointOfInterest;

import java.util.List;

public class PointOfInterestRepository {

    private final PointOfInterestDAO pointsDAO;

    public PointOfInterestRepository(Application application) {
        RoomDB db = RoomDB.getDatabase(application);
        pointsDAO = db.pointOfInterestDAO();
    }

    public void insert(PointOfInterest point, List<Media> medias) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            pointsDAO.insertPoint(point);
            pointsDAO.insertMedias(medias);
        });
    }

    public LiveData<FullPointOfInterest> getPointById(int id){
        return pointsDAO.getPointById(id);
    }

    public LiveData<List<FullPointOfInterest>> getAllPoints(){
        return pointsDAO.getPointOfInterest();
    }
}