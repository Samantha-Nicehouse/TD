package com.example.treasuredetector.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.treasuredetector.dao.ItemDao;
import com.example.treasuredetector.model.Item;

import java.util.concurrent.Executors;

@Database(entities = {Item.class}, version = 14)
public abstract class TreasureDetectorDatabase extends RoomDatabase {
    //creates a singleton of the item database - used everywhere in app
    private static TreasureDetectorDatabase instance;
    public abstract ItemDao itemDao();


    //create database singleton
    //synchronized means only one thread can access this
    //use this method to access database methods in DAO which are in repository clas
    public static synchronized TreasureDetectorDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TreasureDetectorDatabase.class, "treasure_database")
                    .fallbackToDestructiveMigration()//deletes and recreates the database
                    .addCallback(new Callback() {
                        @Override //callback the room database to create it one time only async task replated by executro
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                        }
                    }).build();
        }
        return instance;
    }


}
