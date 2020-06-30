package com.example.cats.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cats.database.entities.Component;

import java.util.List;

@Dao
public interface ComponentDao {
    @Insert
    void insert(Component... components);

    @Query("SELECT * FROM components WHERE id = :id AND isValid = 1")
    LiveData<Component> getById(int id);

    @Query("SELECT * FROM components WHERE type = :type AND isValid = 1")
    LiveData<List<Component>> getByType(int type);

    @Query("SELECT * FROM components WHERE type = :type AND subtype = :subtype AND isValid = 1")
    LiveData<List<Component>> getByTypeAndSubtype(int type, int subtype);

    @Query("SELECT * FROM components WHERE userId = :userId AND isValid = 1")
    LiveData<List<Component>> getByUserId(int userId);
}
