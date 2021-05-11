package com.example.treasuredetector.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;

import com.example.treasuredetector.database.TreasureDetectorDatabase;
import com.example.treasuredetector.dao.ItemDao;
import com.example.treasuredetector.model.Item;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ItemRepository {

    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;
    private Executor executorService;
    private Handler mainThreadHandler;

    public ItemRepository(Application application)
    {
        TreasureDetectorDatabase database = TreasureDetectorDatabase.getInstance(application);
        itemDao = database.itemDao(); //assign database
        allItems = itemDao.getAllItems();
        executorService = Executors.newFixedThreadPool(4);
        mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    }
//the following methods are the API that passes items to the repository abstractions layer
    //executes a thread to insert the item
    public long insert(Item item)
    {
        return itemDao.insert(item);
    }
    public int update(Item item)
    {
        return itemDao.update(item);

    }
    public int delete(Item item)
    {
        return itemDao.delete(item);

    }
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public void deleteAllItems()
    {
        executorService.execute(() -> itemDao.deleteAllItems());

    }

}
