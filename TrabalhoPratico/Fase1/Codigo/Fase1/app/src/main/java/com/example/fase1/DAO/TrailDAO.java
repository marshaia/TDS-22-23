package com.example.fase1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fase1.Data.Edge;
import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.FullTrail;
import com.example.fase1.Data.InfoPoint;
import com.example.fase1.Data.InfoTrail;
import com.example.fase1.Data.Media;
import com.example.fase1.Data.PointOfInterest;
import com.example.fase1.Data.Trail;

import java.util.List;

@Dao
public interface TrailDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrail(Trail trail);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEdges(List<Edge> edges);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPoint(PointOfInterest point);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMedia(Media media);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTrailInfos(List<InfoTrail> infos);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPointInfo(InfoPoint info);

    @Query("DELETE FROM trails_table")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM trails_table")
    LiveData<List<FullTrail>> getTrails();

    @Transaction
    @Query("SELECT * FROM trails_table WHERE id = :id")
    LiveData<FullTrail> getTrailById(int id);


    @Transaction
    @Query("SELECT * FROM points_table WHERE id = :id")
    LiveData<FullPointOfInterest> getPointById(int id);

    @Transaction
    @Query("SELECT DISTINCT p.* FROM trails_table t JOIN edge_table e ON t.id = e.edge_trail JOIN points_table p ON e.edge_start_id = p.id OR e.edge_end_id = p.id WHERE t.id = :trail_id ORDER BY e.id")
    LiveData<List<FullPointOfInterest>> getTrailPoints(int trail_id);

    @Transaction
    @Query("SELECT id FROM trails_table WHERE started")
    LiveData<List<Integer>> getTrailsStarted();

}