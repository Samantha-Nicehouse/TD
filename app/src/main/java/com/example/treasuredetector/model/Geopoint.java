package com.example.treasuredetector.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "location_table")
public class Geopoint {
    @PrimaryKey(autoGenerate = true)
    private int point_id;
    double lat;
    double lon;



    public Geopoint(){}



    public Geopoint(double lat, double lon) {

        this.lat = lat;
        this.lon = lon;
    }





    public int getPoint_id(){
        return point_id;}

    public void setPoint_id(int point_id) {
        this.point_id = point_id;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }


    public void setLon(double lon) {
        this.lon = lon;
    }
}
