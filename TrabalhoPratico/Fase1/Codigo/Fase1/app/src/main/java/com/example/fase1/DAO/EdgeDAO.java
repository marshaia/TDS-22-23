package com.example.fase1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fase1.Data.Edge;

import java.util.List;

@Dao
public interface EdgeDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Edge edge);

    @Query("DELETE FROM edge_table")
    void deleteAll();

    @Query("SELECT * FROM edge_table")
    LiveData<List<Edge>> getEdges();
}