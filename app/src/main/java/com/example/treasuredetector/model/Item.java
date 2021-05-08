package com.example.treasuredetector.model;
import androidx.room.ColumnInfo;
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

    private String title;

    private String description;

    private String category;

    private long time;

    private double latitude;

    private double longitude;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;


    public Item(String title, String description, String category, long time, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public long getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public byte[] getImage() {
        return image;
    }
}

