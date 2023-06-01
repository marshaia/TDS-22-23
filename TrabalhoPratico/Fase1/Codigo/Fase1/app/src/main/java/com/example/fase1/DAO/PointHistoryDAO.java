package com.example.fase1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fase1.Data.PointHistory;

import java.util.List;

@Dao
public interface PointHistoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PointHistory point);

    @Query("DELETE FROM point_history_table")
    void deleteAll();

    @Query("SELECT * FROM point_history_table WHERE isPremium = :isPremium")
    LiveData<List<PointHistory>> getPointHistory(boolean isPremium);

    @Query("SELECT * FROM point_history_table where id = :id")
    LiveData<PointHistory> getPointHistoryById(int id);
}