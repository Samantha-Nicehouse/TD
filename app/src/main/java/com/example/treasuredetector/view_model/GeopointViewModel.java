package com.example.treasuredetector.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.treasuredetector.GeopointRepository;
import com.example.treasuredetector.model.Geopoint;


import java.util.List;

//dont pass a context in application because application can outlive the context
//keeps information  on change of language or change of screen configuration
public class GeopointViewModel extends AndroidViewModel {



        //variables
        GeopointRepository repository;
        private LiveData<List<Geopoint>> points;

        //constructor
        public GeopointViewModel(@NonNull Application application) {
            super(application);

            repository = new GeopointRepository(application);
            points = repository.getAllPoints();
        }

        //activity only has references to the viewmodel not to the repository
        //so we create these wrapper methods
        public void insert(Geopoint point)
        {
            repository.insert(point);
        }

        public void update(Geopoint point)
        {
            repository.update(point);
        }

        public void delete(Geopoint point)
        {
            repository.delete(point);
        }

        public void deleteAllPoints()
        {
            repository.deleteAllPoints();
        }


        public LiveData<List<Geopoint>> getAllPoints() {
            return points;
        }
    }



