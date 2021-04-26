package com.example.treasuredetector;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.treasuredetector.model.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("DELETE FROM item_table")
    void deleteAllItems();


    //live data creates an observable so we know when a new item is created
    @Query("SELECT * FROM item_table ORDER BY mCurrentDate DESC")
    LiveData<List<Item>> getAllItems();
}
