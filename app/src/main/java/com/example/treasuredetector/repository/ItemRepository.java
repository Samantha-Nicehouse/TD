package com.example.treasuredetector.repository;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;

import com.example.treasuredetector.database.TreasureDetectorDatabase;
import com.example.treasuredetector.dao.ItemDao;
import com.example.treasuredetector.helper.Helper;
import com.example.treasuredetector.model.Item;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ItemRepository {

    private final ItemDao itemDao;
    private final LiveData<List<Item>> allItems;
    private final Executor executorService;
    private final Handler mainThreadHandler;
    private Callback callback;
    private CallbackMap callbackMap;
    private final Helper helper;

    public ItemRepository(Application application)
    {
        TreasureDetectorDatabase database = TreasureDetectorDatabase.getInstance(application);
        itemDao = database.itemDao(); //assign database
        allItems = itemDao.getAllItems();
        executorService = Executors.newFixedThreadPool(5);
        mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
        helper = new Helper(application);

    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public void setCallbackMap(CallbackMap callbackMap){
        this.callbackMap = callbackMap;
    }

    // I think we are all clear, we can end the meeting now, bye
    //the following methods are the API that passes items to the repository abstractions layer
    //executes a thread to insert the item
    public void insert(Item item, Bitmap bitmap)
    {
        executorService.execute(() -> {

            if (bitmap != null) {
                String imageName = item.getTitle() + "_" + item.getTime();
                String imagePath = helper.saveImageToStorage(bitmap, imageName);

                if (imagePath != null && !imagePath.isEmpty()) {
                    item.setImagePath(imagePath);
                    item.setImageName(imageName);
                }
            }

            itemDao.insert(item);

            mainThreadHandler.post(() -> callback.onItemAdded());
        });
    }
    public void update(Item item, Bitmap bitmap)
    {
        executorService.execute(() -> {
            if (bitmap != null) {

                //If there was an image already associated with this entry we will delete it
                if(item.getImageName()!= null){
                    helper.deleteImageFromStorage(item.getImagePath(), item.getImageName());
                }

                String imageName = item.getTitle() + "_" + item.getTime();
                String imagePath = helper.saveImageToStorage(bitmap, imageName);

                if (imagePath != null && !imagePath.isEmpty()) {
                    item.setImagePath(imagePath);
                    item.setImageName(imageName);
                }
            }

            itemDao.update(item);

            mainThreadHandler.post(() -> callback.onItemUpdated());
        });

    }
    public void delete(Item item)
    {
        executorService.execute(() -> {
            //If there is an image associated with this entry we will delete it
            if(item.getImageName()!= null){
                helper.deleteImageFromStorage(item.getImagePath(), item.getImageName());
            }
            itemDao.delete(item);

            mainThreadHandler.post(() -> callback.onItemDeleted());
        });

    }
    public void getLastFiveEntries()
    {
        executorService.execute(() -> {

            List<Item> list = itemDao.getLastFiveEntries();

            mainThreadHandler.post(() -> callbackMap.onItemsFetched(list));
        });
    }
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public interface CallbackMap{
        void onItemsFetched(List<Item> list);
    }

    public interface Callback{
        void onItemAdded();
        void onItemUpdated();
        void onItemDeleted();
    }

}
