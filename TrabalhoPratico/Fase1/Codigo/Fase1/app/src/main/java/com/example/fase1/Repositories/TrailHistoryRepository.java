package com.example.fase1.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fase1.DAO.TrailHistoryDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.TrailHistory;

import java.util.List;

public class TrailHistoryRepository {

    private final TrailHistoryDAO trailHistoryDAO;
    private LiveData<List<TrailHistory>> allHistory;

    public TrailHistoryRepository(Application application, boolean isPremium) {
        RoomDB db = RoomDB.getDatabase(application);
        trailHistoryDAO = db.trailHistoryDAO();
        allHistory = trailHistoryDAO.getTrailHistory(isPremium);
    }

    public LiveData<List<TrailHistory>> getAllHistory() {
        return allHistory;
    }

    public void insert(TrailHistory trailHistory) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            trailHistoryDAO.insert(trailHistory);
        });
    }

    public LiveData<TrailHistory> getLastTrailHistory(int trail_id){
        return trailHistoryDAO.getLastTrailHistory(trail_id);
    }
}