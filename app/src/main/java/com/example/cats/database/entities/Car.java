package com.example.cats.database.entities;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "car",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId"
                )
        }
)
public class Car {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "userId", index = true)
    public int userId;

    @ColumnInfo(name = "bodyId", index = true)
    public int bodyId;

    @ColumnInfo(name = "componentId1", index = true)
    public int componentId1;

    @ColumnInfo(name = "componentId2", index = true)
    public int componentId2;

    @ColumnInfo(name = "componentId3", index = true)
    public int componentId3;

    @ColumnInfo(name = "frontWheelId", index = true)
    public int frontWheelId;

    @ColumnInfo(name = "backWheelId", index = true)
    public int backWheelId;

    @ColumnInfo(name = "isValid")
    public boolean isValid;

    public Car(int userId, int bodyId, int componentId1, int componentId2, int componentId3, int frontWheelId, int backWheelId) {
        this.userId = userId;
        this.bodyId = bodyId;
        this.componentId1 = componentId1;
        this.componentId2 = componentId2;
        this.componentId3 = componentId3;
        this.frontWheelId = frontWheelId;
        this.backWheelId = backWheelId;
        this.isValid = true;
    }
}
