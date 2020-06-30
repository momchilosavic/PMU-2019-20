package com.example.cats.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="mail")
    public String mail;

    @ColumnInfo(name="username")
    public String username;

    @ColumnInfo(name="password")
    public String password;

    @ColumnInfo(name = "isValid")
    public boolean isValid;

    public User(String mail, String username, String password){
        this.mail = mail;
        this.username = username;
        this.password = password;
        this.isValid = true;
    }
}
