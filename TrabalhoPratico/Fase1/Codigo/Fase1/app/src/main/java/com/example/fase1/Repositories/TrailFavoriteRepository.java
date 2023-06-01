package com.example.fase1.Repositories;


import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fase1.DAO.TrailFavoriteDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.TrailFavorite;

import java.util.List;

public class TrailFavoriteRepository {

    private final TrailFavoriteDAO trailFavoriteDAO;
    private LiveData<List<TrailFavorite>> allFavorite;

    public TrailFavoriteRepository(Application application, boolean isPremium) {
        RoomDB db = RoomDB.getDatabase(application);
        trailFavoriteDAO = db.trailFavoriteDAO();
        allFavorite = trailFavoriteDAO.getFavorites(isPremium);
    }

    public LiveData<List<TrailFavorite>> getAllFavorites() {
        return allFavorite;
    }

    public void insert(TrailFavorite trailFavorite) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            trailFavoriteDAO.insert(trailFavorite);
        });
    }

    public void deleteFavorite(int id, boolean isPremium){
        RoomDB.databaseWriteExecutor.execute(() -> {
            trailFavoriteDAO.deleteFavorite(id,isPremium);
        });
    }

    public LiveData<List<TrailFavorite>> getAllFavoritesFromTrail(int id, boolean isPremium){
        return trailFavoriteDAO.getAllFavoritesFromTrail(id,isPremium);
    }

}
