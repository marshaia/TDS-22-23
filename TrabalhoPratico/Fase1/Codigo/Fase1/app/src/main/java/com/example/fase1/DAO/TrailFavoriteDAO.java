package com.example.fase1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fase1.Data.TrailFavorite;

import java.util.List;

@Dao
public interface TrailFavoriteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrailFavorite fav);

    @Delete
    void delete(TrailFavorite fav);

    @Query("DELETE FROM trail_favorite_table")
    void deleteAll();

    @Query("SELECT * FROM trail_favorite_table WHERE isPremium = :isPremium")
    LiveData<List<TrailFavorite>> getFavorites(boolean isPremium);

    @Query("SELECT * FROM trail_favorite_table WHERE isPremium = :isPremium AND trail_id = :trail_id")
    LiveData<List<TrailFavorite>> getAllFavoritesFromTrail(int trail_id, boolean isPremium);

    @Transaction
    @Query("DELETE FROM trail_favorite_table WHERE trail_id = :id AND isPremium = :isPremium")
    void deleteFavorite(int id, boolean isPremium);


}
