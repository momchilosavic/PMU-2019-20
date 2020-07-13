package com.example.cats.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cats.database.entities.Stats;

import java.util.List;

@Dao
public interface StatsDao {
    @Insert
    void insert(Stats... stats);

    @Query("SELECT * FROM stats WHERE id = :id AND isValid = 1")
    LiveData<List<Stats>> getById(int id);

    @Query("SELECT * FROM stats WHERE userId = :userId AND isValid = 1 ORDER BY time ASC")
    LiveData<List<Stats>> getByUserId(int userId);

}
