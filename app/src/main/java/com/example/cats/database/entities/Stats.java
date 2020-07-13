package com.example.cats.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity(
        tableName = "stats",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE))
public class Stats {
    public static enum Result {LOSS, WIN};

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "userId", index = true)
    public int userId;

    @ColumnInfo(name = "result")
    public int result;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "isValid")
    public boolean isValid;

    public Stats(int userId, int result, String time){
        this.userId = userId;
        this.result = result;
        this.time = time;
        isValid = true;
    }
}
