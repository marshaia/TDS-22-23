package com.example.fase1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fase1.Data.InfoTrail;

import java.util.List;

@Dao
public interface InfoTrailDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(InfoTrail infoTrail);

    @Transaction
    @Query("DELETE FROM info_trail_table")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM info_trail_table")
    LiveData<List<InfoTrail>> getAllInfoTrail();

}