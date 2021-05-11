package com.example.treasuredetector.dao;

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
    long insert(Item item);

    @Update
    int update(Item item);

    @Delete
    int delete(Item item);

    @Query("DELETE FROM item_table")
    void deleteAllItems();

    //live data creates an observable so we know when a new item is created
    @Query("SELECT * FROM item_table")
    LiveData<List<Item>> getAllItems();
}
