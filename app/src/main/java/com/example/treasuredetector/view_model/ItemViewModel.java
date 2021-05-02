package com.example.treasuredetector.view_model;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.treasuredetector.ItemRepository;
import com.example.treasuredetector.model.Item;

import java.util.List;

//dont pass a context in aoolication because application can outlive the context
//keeps information  on change of language or change of screen configuration
public class ItemViewModel extends AndroidViewModel {

    //variables
    ItemRepository repository;
    private LiveData<List<Item>> allItems;

    //constructor
    public ItemViewModel(@NonNull Application application) {
        super(application);

        repository = new ItemRepository(application);
        allItems = repository.getAllItems();
    }

    //activity only has references to the viewmodel not to the repository
    //so we create these wrapper methods
    public void insert(Item item)
    {
        repository.insert(item);
    }

    public void update(Item item)
    {
        repository.update(item);
    }

    public void delete(Item item)
    {
        repository.delete(item);
    }

    public void deleteAll()
    {
        repository.deleteAllItems();
    }


    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }
}