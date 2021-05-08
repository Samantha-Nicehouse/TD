package com.example.treasuredetector.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.treasuredetector.model.User;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Query("select * from user_table where username = :username and password = :password")
    User login(String username, String password);
}
