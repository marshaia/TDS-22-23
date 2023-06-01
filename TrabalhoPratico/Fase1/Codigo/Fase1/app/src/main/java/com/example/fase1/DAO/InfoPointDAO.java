package com.example.fase1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fase1.Data.InfoPoint;

import java.util.List;

@Dao
public interface InfoPointDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(InfoPoint infoPoint);

    @Transaction
    @Query("DELETE FROM info_point_table")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM info_point_table")
    LiveData<List<InfoPoint>> getAllInfoPoint();


}
