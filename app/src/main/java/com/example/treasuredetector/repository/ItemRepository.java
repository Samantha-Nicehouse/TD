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

import javax.xml.transform.Result;

public class ItemRepository {

    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;
    private Executor executorService;
    private Handler mainThreadHandler;
    private Callback callback;
    private Helper helper;

    public ItemRepository(Application application)
    {
        TreasureDetectorDatabase database = TreasureDetectorDatabase.getInstance(application);
        itemDao = database.itemDao(); //assign database
        allItems = itemDao.getAllItems();
        executorService = Executors.newFixedThreadPool(4);
        mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
        helper = new Helper(application);

    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    // I think we are all clear, we can end the meeting now, bye
    //the following methods are the API that passes items to the repository abstractions layer
    //executes a thread to insert the item
    public void insert(Item item, Bitmap bitmap)
    {
//        executorService.execute(() ->itemDao.insert(item));
        executorService.execute(() -> {

            if (bitmap != null) {
                String imageName = item.getTitle() + "_" + item.getTime();
                String imagePath = helper.saveToInternalStorage(bitmap, imageName);

                if (imagePath != null && !imagePath.isEmpty()) {
                    item.setImagePath(imagePath);
                    item.setImageName(imageName);
                }
            }

            itemDao.insert(item);

            mainThreadHandler.post(() -> {
                callback.onItemAdded();
            });
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
                String imagePath = helper.saveToInternalStorage(bitmap, imageName);

                if (imagePath != null && !imagePath.isEmpty()) {
                    item.setImagePath(imagePath);
                    item.setImageName(imageName);
                }
            }

            itemDao.update(item);

            mainThreadHandler.post(() -> {
                callback.onItemUpdated();
            });
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

            mainThreadHandler.post(() -> {
                callback.onItemDeleted();
            });
        });

    }
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public void deleteAllItems()
    {
        executorService.execute(() -> itemDao.deleteAllItems());

    }

    public interface Callback{
        void onItemAdded();
        void onItemUpdated();
        void onItemDeleted();
    }

}
