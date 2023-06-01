package com.example.fase1.Repositories;

import android.app.Application;

import com.example.fase1.DAO.MediaDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.Media;


public class MediaRepository {

    private final MediaDAO mediaDao;

    public MediaRepository(Application application) {
        RoomDB db = RoomDB.getDatabase(application);
        mediaDao = db.mediaDAO();
    }

    public void insert(Media media) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            mediaDao.insert(media);
        });
    }
}
