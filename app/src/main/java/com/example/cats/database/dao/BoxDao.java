package com.example.cats.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cats.database.entities.Box;

import java.util.List;

@Dao
public interface BoxDao {
    @Insert
    void insert(Box... boxes);

    @Query("SELECT * FROM boxes WHERE userId = :userId AND isValid = 1")
    LiveData<List<Box>> getByUserId(int userId);

    @Query("UPDATE boxes SET isValid = 0 WHERE id = :id")
    void removeById(int id);
}
