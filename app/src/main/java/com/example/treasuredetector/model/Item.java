package com.example.treasuredetector.model;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "item_table")
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int mImageResource;

    public int getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    private String mName;
    @Ignore
    public Calendar calendar = Calendar.getInstance();
    private String mCurrentDate;


    public Item(int imageResource, String name) {
        mImageResource = imageResource;
        mName = name;
        mCurrentDate = DateFormat.getDateInstance().format(calendar.getTime());;
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

