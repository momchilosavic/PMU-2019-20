package com.example.cats.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cats.database.dao.BoxDao;
import com.example.cats.database.dao.CarDao;
import com.example.cats.database.dao.ComponentDao;
import com.example.cats.database.dao.StatsDao;
import com.example.cats.database.dao.UserDao;
import com.example.cats.database.entities.Box;
import com.example.cats.database.entities.Car;
import com.example.cats.database.entities.Component;
import com.example.cats.database.entities.Stats;
import com.example.cats.database.entities.User;
import com.example.cats.database.prepopulate.ComponentsCallback;

@Database(
        entities = {User.class, Component.class, Stats.class, Box.class, Car.class},
        exportSchema = false,
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "cats_db";
    private static AppDatabase singletonInstance = null;

    abstract public UserDao UserDao();
    abstract public ComponentDao ComponentDao();
    abstract public CarDao CarDao();
    abstract public BoxDao BoxDao();
    abstract public StatsDao StatsDao();

    public static synchronized AppDatabase getInstance(Context context){
        if(singletonInstance == null){
            singletonInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME)./*addCallback(new ComponentsCallback()).*/build();
        }
        return singletonInstance;
    }
}
