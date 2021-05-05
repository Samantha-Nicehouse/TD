package com.example.treasuredetector.model;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "item_table")
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int item_id;
    private int mImageResource;
   @Embedded
    public Geopoint geopoint;

    private String mName;
    @Ignore
    public Calendar calendar = Calendar.getInstance();
    private String mCurrentDate;

    public Item(int imageResource, String name, String currentDate) {
        mImageResource = imageResource;
        mName = name;
        mCurrentDate = currentDate;
       // geopoint.setLat(lat);
       // geopoint.setLon(lon);
       // geopoint.setId_fkItem(item_id);

    }



    public void setImageResource(int ImageResource) {
        this.mImageResource = ImageResource;
    }


    public int getImageResource() {
        return mImageResource;
    }
    public String getName() {
        return mName;
    }
    public String getCurrentDate() {
        return mCurrentDate;
    }

    public void setCurrentDate(String mCurrentDate) {
        this.mCurrentDate = mCurrentDate;
    }

    public void setItem_id(int id) {
        this.item_id = id;
    }

    public int getItem_id() {
        return item_id;
    }



}

