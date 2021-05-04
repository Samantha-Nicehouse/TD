package com.example.treasuredetector;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.treasuredetector.model.Geopoint;
import com.example.treasuredetector.model.Item;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeopointRepository {
    private GeopointDao geopointDao;
    private LiveData<List<Geopoint>> points;
    private Executor executorService;
    private Handler mainThreadHandler;

    public GeopointRepository(Application application)
    {
        ItemDatabase database = ItemDatabase.getInstance(application);
        geopointDao = database.geopointDao(); //assign database
        points= geopointDao.getAllPoints();
        executorService = Executors.newFixedThreadPool(4);
        mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    }
    //the following methods are the API that passes items to the repository abstractions layer
    //executes a thread to insert the item
    public void insert(Geopoint point)
    {
        executorService.execute(() -> geopointDao.insert(point));
    }

    public void update(Geopoint point)
    {
        executorService.execute(() -> geopointDao.update(point));

    }
    public void delete(Geopoint point)
    {
        executorService.execute(() -> geopointDao.delete(point));

    }
    public LiveData<List<Geopoint>> getAllPoints() {
        return points;
    }

    public void deleteAllPoints()
    {
        executorService.execute(() -> geopointDao.deleteAllPoints());

    }


}
