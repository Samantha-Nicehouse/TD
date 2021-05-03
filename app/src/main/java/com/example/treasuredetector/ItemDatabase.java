package com.example.treasuredetector;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.treasuredetector.model.Geopoint;
import com.example.treasuredetector.model.Item;

import java.util.concurrent.Executors;

@Database(entities = {Item.class, Geopoint.class}, version = 3)
public abstract class ItemDatabase extends RoomDatabase {
    //creates a singleton of the item database - used everywhere in app
    private static ItemDatabase instance;
    public abstract ItemDao itemDao();
    public abstract GeopointDao geopointDao();

    //create database singleton
    //synchronized means only one thread can access this
    //use this method to access database methods in DAO which are in repository clas
    public static synchronized ItemDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ItemDatabase.class, "treasure_database")
                    .fallbackToDestructiveMigration()//deletes and recreates the database
                    .addCallback(new Callback() {
                        @Override //callback the room database to create it one time only async task replated by executro
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    getInstance(context).itemDao().insert(new Item(R.drawable.ic_jewelry, "Jewelry", ""));
                                    getInstance(context).geopointDao().insert(new Geopoint("Home", 156.1512448,0.2137856));
                                }
                            });
                        }
                    }).build();
        }
        return instance;
    }


}
