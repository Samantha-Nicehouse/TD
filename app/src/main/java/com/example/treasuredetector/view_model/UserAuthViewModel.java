package com.example.treasuredetector.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.treasuredetector.repository.UserAuthRepository;

public class UserAuthViewModel extends AndroidViewModel {

    UserAuthRepository userAuthRepository;

    public UserAuthViewModel(@NonNull Application application) {
        super(application);

        userAuthRepository = new UserAuthRepository();
    }

    public void registerUser(String email, String password, String name) {
        userAuthRepository.registerUser(email, password, name);
    }

    public void loginUser(String email, String password) {
        userAuthRepository.loginUser(email, password);
    }

    public void setCallbackRegister(UserAuthRepository.CallbackRegister callbackRegister) {
        userAuthRepository.setCallbackRegister(callbackRegister);
    }

    public void setCallbackLogin(UserAuthRepository.CallbackLogin callbackLogin) {
        userAuthRepository.setCallbackLogin(callbackLogin);
    }
}
