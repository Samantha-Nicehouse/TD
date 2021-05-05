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
   private int id_fkItem;


    public Geopoint(){}



    public Geopoint(double lat, double lon) {

        this.lat = lat;
        this.lon = lon;
    }



   public int getId_fkItem() {
        return id_fkItem;
    }

    public void setId_fkItem(int id_fkItem) {
        this.id_fkItem = id_fkItem;
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
