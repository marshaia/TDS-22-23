package com.example.fase1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fase1.Data.TrailHistory;

import java.util.List;

@Dao
public interface TrailHistoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrailHistory trailHistory);

    @Query("DELETE FROM trail_history_table")
    void deleteAll();

    @Query("SELECT * FROM trail_history_table WHERE isPremium = :isPremium ORDER BY last_update DESC")
    LiveData<List<TrailHistory>> getTrailHistory(boolean isPremium);

    @Query("SELECT * FROM trail_history_table WHERE trail_id = :trail_id AND last_update = (SELECT MAX(last_update) FROM trail_history_table WHERE trail_id = :trail_id) LIMIT 1")
    LiveData<TrailHistory> getLastTrailHistory(int trail_id);

}