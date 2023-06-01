package com.example.fase1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fase1.Data.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("SELECT * from user_table LIMIT 1")
    LiveData<User> getUser();

    @Query("SELECT * from user_table")
    LiveData<List<User>> getUsers();


}
