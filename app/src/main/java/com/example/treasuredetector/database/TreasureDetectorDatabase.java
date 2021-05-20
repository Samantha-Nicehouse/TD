package com.example.treasuredetector.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.treasuredetector.dao.ItemDao;
import com.example.treasuredetector.model.Item;

import java.util.Date;
import java.util.concurrent.Executors;

@Database(entities = {Item.class}, version = 3)
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
                            Executors.newSingleThreadScheduledExecutor().execute(() -> {
                                getInstance(context).itemDao().insert(new Item("Excaliber", "King Arthur's sword", "Sword", new Date().getTime(), 56.196031, 10.240737));
                                getInstance(context).itemDao().insert(new Item("Precious", "Tumbler key with bow", "Key", new Date().getTime(), 56.167834, 10.215146));
                                getInstance(context).itemDao().insert(new Item("Jackpot", "This is going to be worth so much money", "Miscellaneous", new Date().getTime(), 56.13146711, 10.20554611));
                                getInstance(context).itemDao().insert(new Item("Silver Coins", "18th century silver coins", "Coins", new Date().getTime(), 56.13055711, 10.20762311));
                                getInstance(context).itemDao().insert(new Item("Ring", "Size 6 ring", "Jewelry", new Date().getTime(), 56.12906811, 10.19494211));

                            });
                        }
                    }).build();
        }

        return instance;
    }


}
