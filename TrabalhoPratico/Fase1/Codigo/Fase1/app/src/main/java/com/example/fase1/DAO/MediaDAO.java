package com.example.fase1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fase1.Data.Media;

import java.util.List;

@Dao
public interface MediaDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Media media);

    @Query("DELETE FROM media_table")
    void deleteAll();

    @Query("SELECT * FROM media_table")
    LiveData<List<Media>> getMedias();
}