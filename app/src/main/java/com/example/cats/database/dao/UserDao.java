package com.example.cats.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cats.database.entities.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE mail = :mail AND password = :password AND isValid = 1")
    LiveData<User> login(String mail, String password);

    @Insert()
    void register(User... users);

    @Query("SELECT COUNT(*) FROM users WHERE mail = :mail AND isValid = 1")
    LiveData<Integer> getCount(String mail);

    @Query("SELECT password FROM users WHERE mail = :mail AND isValid = 1")
    LiveData<String> getPasswordByMail(String mail);

    @Query("SELECT username FROM users WHERE id = :id")
    LiveData<String> getUsernameById(int id);
}
