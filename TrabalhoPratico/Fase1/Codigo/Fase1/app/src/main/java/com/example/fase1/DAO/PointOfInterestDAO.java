package com.example.fase1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.Media;
import com.example.fase1.Data.PointOfInterest;

import java.util.List;

@Dao
public interface PointOfInterestDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPoint(PointOfInterest pointOfInterest);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMedias(List<Media> medias);

    @Query("DELETE FROM points_table")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM points_table")
    LiveData<List<FullPointOfInterest>> getPointOfInterest();

    @Transaction
    @Query("SELECT * FROM points_table where id = :id")
    LiveData<FullPointOfInterest> getPointById(int id);
}