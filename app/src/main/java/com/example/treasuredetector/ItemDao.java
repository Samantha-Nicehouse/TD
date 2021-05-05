package com.example.treasuredetector;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.treasuredetector.model.Geopoint;
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


   /* @Query("SELECT * FROM location_table WHERE id_fkGeopoint =:id")
    LiveData<List<Geopoint>> getAllPoints(int id);*/

    //live data creates an observable so we know when a new item is created
    @Query("SELECT * FROM item_table")
    LiveData<List<Item>> getAllItems();
}
