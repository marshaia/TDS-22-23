package com.example.fase1.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fase1.DAO.PointHistoryDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.PointHistory;

import java.util.List;

public class PointHistoryRepository {

    private final PointHistoryDAO pointHistoryDAO;
    private LiveData<List<PointHistory>> allHistory;

    public PointHistoryRepository(Application application, boolean isPremium) {
        RoomDB db = RoomDB.getDatabase(application);
        pointHistoryDAO = db.pointHistoryDAO();
        allHistory = pointHistoryDAO.getPointHistory(isPremium);
    }

    public LiveData<List<PointHistory>> getAllHistory() {
        return allHistory;
    }

    public void insert(PointHistory pointHistory) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            pointHistoryDAO.insert(pointHistory);
        });
    }
}
