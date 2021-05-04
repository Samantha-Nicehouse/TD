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
    private int id;
    private int mImageResource;
  //  @Embedded
   // public Geopoint geopoint;

    private String mName;
    @Ignore
    public Calendar calendar = Calendar.getInstance();
    private String mCurrentDate;

    public Item(int imageResource, String name, String currentDate) {
        mImageResource = imageResource;
        mName = name;
        mCurrentDate = currentDate;

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

    public void setId(int id) {
        this.id = id;
    }
    public int getId(){return id;}

}

