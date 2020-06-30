package com.example.cats.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cats.database.entities.Car;

@Dao
public interface CarDao {
    @Insert
    void insert(Car... cars);

    @Query("SELECT * FROM car WHERE userId = :userId AND isValid = 1")
    LiveData<Car> getByUserId(int userId);

    @Query("UPDATE car SET bodyId = :bodyId WHERE id = :id")
    void updateBodyById(int id, int bodyId);

    @Query("UPDATE car SET componentId1 = :componentId1 WHERE id = :id")
    void updateComponent1ById(int id, int componentId1);

    @Query("UPDATE car SET componentId2 = :componentId2 WHERE id = :id")
    void updateComponent2ById(int id, int componentId2);

    @Query("UPDATE car SET componentId3 = :componentId3 WHERE id = :id")
    void updateComponent3ById(int id, int componentId3);

    @Query("UPDATE car SET frontWheelId = :frontWheelId WHERE id = :id")
    void frontWheelId(int id, int frontWheelId);

    @Query("UPDATE car SET backWheelId = :backWheelId WHERE id = :id")
    void backWheelId(int id, int backWheelId);
}
