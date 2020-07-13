package com.example.cats.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cats.database.entities.Box;

import java.util.List;

@Dao
public interface BoxDao {
    @Insert
    void insert(Box... boxes);

    @Query("SELECT * FROM boxes WHERE userId = :userId AND isValid = 1 ORDER BY time ASC")
    LiveData<List<Box>> getByUserId(int userId);

    @Query("UPDATE boxes SET isValid = 0 WHERE id = :id")
    void removeById(int id);

    @Update
    void update(Box box);
}
