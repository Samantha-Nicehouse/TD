package com.example.treasuredetector.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.treasuredetector.database.TreasureDetectorDatabase;
import com.example.treasuredetector.model.User;
import com.example.treasuredetector.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        TreasureDetectorDatabase database = TreasureDetectorDatabase.getInstance(application);

        userRepository = new UserRepository(application);
    }

    public long registerUser(User user){
        return userRepository.insert(user);
    }

    public User login(String username, String password){
        return userRepository.login(username, password);
    }
}
