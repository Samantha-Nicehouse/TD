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
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("DELETE FROM item_table")
    void deleteAllItems();

    //This method returns max last five items from db
    @Query("select * from item_table order by id desc limit 5")
    LiveData<List<Item>> getLastFiveEntries();

    //live data creates an observable so we know when a new item is created
    @Query("SELECT * FROM item_table")
    LiveData<List<Item>> getAllItems();


}
