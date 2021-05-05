package com.example.treasuredetector.model;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import static androidx.room.ForeignKey.CASCADE;

import java.util.Calendar;

@Entity(tableName = "item_table")
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int item_id;
    private int mImageResource;
    private String mImageURI;

  // private int id_fkGeopoint;

    private String mName;
    @Ignore
    public Calendar calendar = Calendar.getInstance();
    private String mCurrentDate;


    public Item(int imageResource, String name, String currentDate, String imageURI) {
        mImageResource = imageResource;
        mName = name;
        mCurrentDate = currentDate;
        mImageURI = imageURI;


    }
/*
    public int getId_fkGeopoint(){
        return id_fkGeopoint;
    }

    public int setId_fkGeopoint(){
        this.id_fkGeopoint = id_fkGeopoint;
    }*/

   public String getImageURI(){
        return mImageURI;
    }

    public void setItemPic(String imageURI){
       this.mImageURI = imageURI;
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

