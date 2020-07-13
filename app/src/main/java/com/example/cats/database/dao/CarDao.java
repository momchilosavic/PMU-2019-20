package com.example.cats.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cats.database.entities.Car;

@Dao
public interface CarDao {
    @Insert
    void insert(Car... cars);

    @Query("SELECT * FROM car WHERE userId = :userId AND isValid = 1")
    LiveData<Car> getByUserId(int userId);

    @Query("UPDATE car SET bodyId = :bodyId WHERE userId = :userId")
    void updateBodyByUserId(int userId, int bodyId);

    @Query("UPDATE car SET componentId1 = :componentId1 WHERE userId = :userId")
    void updateComponent1ByUserId(int userId, int componentId1);

    @Query("UPDATE car SET componentId2 = :componentId2 WHERE userId = :userId")
    void updateComponent2ByUserId(int userId, int componentId2);

    @Query("UPDATE car SET componentId3 = :componentId3 WHERE userId = :userId")
    void updateComponent3ByUserId(int userId, int componentId3);

    @Query("UPDATE car SET frontWheelId = :frontWheelId WHERE userId = :userId")
    void updateFrontWheelByUserId(int userId, int frontWheelId);

    @Query("UPDATE car SET backWheelId = :backWheelId WHERE userId = :userId")
    void updateBackWheelByUserId(int userId, int backWheelId);

    @Update
    public void update(Car car);

    @Query("SELECT * FROM car ORDER BY RANDOM() LIMIT 1")
    LiveData<Car>getRandom();
}
