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
public interface GeopointDao {
    @Insert
    void insert(Geopoint point);

    @Update
    void update(Geopoint point);

    @Delete
    void delete(Geopoint point);

    @Query("DELETE FROM location_table")
    void deleteAllPoints();

  /*  @Query("SELECT * FROM location_table WHERE point_id = id_fkGeopoint = :id")
    LiveData<List<Geopoint>> getAllPoints(int id);*/


    //live data creates an observable so we know when a new item is created
    @Query("SELECT * FROM location_table ORDER BY point_id DESC")
    LiveData<List<Geopoint>> getAllPoints();
}
