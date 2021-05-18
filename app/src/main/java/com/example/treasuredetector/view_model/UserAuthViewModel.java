package com.example.treasuredetector.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.treasuredetector.repository.UserAuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class UserAuthViewModel extends AndroidViewModel {

    UserAuthRepository userAuthRepository;
    MutableLiveData<FirebaseUser> userMutableLiveData;

    public UserAuthViewModel(@NonNull Application application) {
        super(application);

        userAuthRepository = new UserAuthRepository(application);
        userMutableLiveData = userAuthRepository.getUserMutableLiveData();
    }

    public void registerUser(String email, String password, String name) {
        userAuthRepository.registerUser(email, password, name);
    }

    public void loginUser(String email, String password) {
        userAuthRepository.loginUser(email, password);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData(){
        return userMutableLiveData;
    }
}
