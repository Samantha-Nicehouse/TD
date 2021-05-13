package com.example.treasuredetector.view_model;


import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.treasuredetector.repository.ItemRepository;
import com.example.treasuredetector.model.Item;

import java.util.List;

//dont pass a context in aoolication because application can outlive the context
//keeps information  on change of language or change of screen configuration
public class ItemViewModel extends AndroidViewModel {

    //variables
    ItemRepository repository;
    private final LiveData<List<Item>> allItems;

    //constructor
    public ItemViewModel(@NonNull Application application) {
        super(application);

        this.repository = new ItemRepository(application);
        allItems = repository.getAllItems();
    }

    public void setCallback(ItemRepository.Callback callback) {
        this.repository.setCallback(callback);
    }

    public void setCallbackMap(ItemRepository.CallbackMap callbackMap){
        this.repository.setCallbackMap(callbackMap);
    }

    //activity only has references to the viewmodel not to the repository
    //so we create these wrapper methods
    public void insert(Item item, Bitmap bitmap)
    {
        repository.insert(item, bitmap);
    }

    public void update(Item item, Bitmap bitmap)
    {
        repository.update(item, bitmap);
    }

    public void delete(Item item)
    {
        repository.delete(item);
    }

    public void getLastFiveEntries(){
        repository.getLastFiveEntries();
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }
}
