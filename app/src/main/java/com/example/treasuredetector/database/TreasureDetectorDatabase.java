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

@Database(entities = {Item.class}, version = 2)
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
                                getInstance(context).itemDao().insert(new Item("Title1", "Description1", "Miscellaneous", new Date().getTime(), 56.15932211, 10.22259811));
                                getInstance(context).itemDao().insert(new Item("Title2", "Description2", "Miscellaneous", new Date().getTime(), 56.16659111, 10.21567511));
                                getInstance(context).itemDao().insert(new Item("Title3", "Description3", "Miscellaneous", new Date().getTime(), 56.13146711, 10.20554611));
                                getInstance(context).itemDao().insert(new Item("Title4", "Description4", "Miscellaneous", new Date().getTime(), 56.13055711, 10.20762311));
                                getInstance(context).itemDao().insert(new Item("Title5", "Description5", "Miscellaneous", new Date().getTime(), 56.12906811, 10.19494211));

                            });
                        }
                    }).build();
        }

        return instance;
    }


}
