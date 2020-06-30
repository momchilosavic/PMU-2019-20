package com.example.cats.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity(
        tableName = "boxes",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId"
        )
)
public class Box {
    public static int OPEN_TIME_IN_SECONDS = 3*60*60;

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "userId", index = true)
    public int userId;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "isValid")
    public boolean isValid;

    public Box(int userId){
        this.userId = id;
        this.time = null;
        this.isValid = true;
    }
}
