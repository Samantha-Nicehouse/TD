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
    private final LiveData<List<Item>> lastFiveItems;
    private final Executor executorService;
    private final Helper helper;

    public ItemRepository(Application application) {
        TreasureDetectorDatabase database = TreasureDetectorDatabase.getInstance(application.getApplicationContext());//changed to .getapplicationcontext so that map callback updates when item updated
        itemDao = database.itemDao(); //assign database
        allItems = itemDao.getAllItems();
        lastFiveItems = itemDao.getLastFiveEntries();
        executorService = Executors.newFixedThreadPool(3);
        helper = new Helper(application.getApplicationContext());// also changed bc of line 29 but dont know if this makes a diff

    }

    //the following methods are the API that passes items to the repository abstractions layer
    //executes a thread to insert the item
    public void insert(Item item, Bitmap bitmap) {
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
        });
    }

    public void update(Item item, Bitmap bitmap) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {

                    //If there was an image already associated with this entry we will delete it
                    if (item.getImageName() != null) {
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
            }
        });

    }

    public void delete(Item item) {
        executorService.execute(() -> {
            //If there is an image associated with this entry we will delete it
            if (item.getImageName() != null) {
                helper.deleteImageFromStorage(item.getImagePath(), item.getImageName());
            }
            itemDao.delete(item);
        });

    }

    public LiveData<List<Item>> getLastFiveEntries() {
        return lastFiveItems;
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }


}
