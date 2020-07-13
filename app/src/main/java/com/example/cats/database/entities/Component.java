package com.example.cats.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "components",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId"
        ))
public class Component {
    public static enum Type {BODY, WHEEL, FORKLIFT, STIGNER, CHAINSAW, ROCKET, BLADE};
    public static enum Subtype {LOW, MEDIUM, HIGH};

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "userId", index = true)
    public int userId;

    @ColumnInfo(name = "type")
    public int type;

    @ColumnInfo(name = "subtype")
    public int subtype;

    @ColumnInfo(name = "health")
    public int health;

    @ColumnInfo(name = "power")
    public int power;

    @ColumnInfo(name = "energy")
    public int energy;

    @ColumnInfo(name = "isValid")
    public boolean isValid;

    public Component(int userId, int type, int subtype, int health, int power, int energy) {
        this.userId = userId;
        this.type = type;
        this.subtype = subtype;
        this.health = health;
        this.power = power;
        this.energy = energy;
        this.isValid = true;
    }
}
