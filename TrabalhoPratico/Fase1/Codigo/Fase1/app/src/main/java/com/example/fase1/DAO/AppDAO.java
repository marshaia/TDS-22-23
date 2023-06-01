package com.example.fase1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fase1.Data.App;
import com.example.fase1.Data.FullApp;
import com.example.fase1.Data.Partner;
import com.example.fase1.Data.Social;

import java.util.List;

@Dao
public interface AppDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertApp(App app);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSocials(List<Social> socials);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPartners(List<Partner> partners);

    @Delete
    void delete(App app);

    @Transaction
    @Query("DELETE FROM app_table")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM app_table")
    LiveData<List<FullApp>> getApp();

}
