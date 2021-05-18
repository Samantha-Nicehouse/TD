package com.example.treasuredetector.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class UserAuthRepository {


    private final Application application;
    private static final String TAG = "UserAuthRepository";
    private final FirebaseAuth mAuth;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    public UserAuthRepository(Application application) {
        this.application = application;
        this.mAuth = FirebaseAuth.getInstance();
        this.userMutableLiveData = new MutableLiveData<>();
    }

    public void registerUser(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        //This assign the name to this newly registered user
                        new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        userMutableLiveData.postValue(mAuth.getCurrentUser());
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(application, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        userMutableLiveData.postValue(null);
                    }
                });
    }

    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        userMutableLiveData.postValue(mAuth.getCurrentUser());
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(application, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        userMutableLiveData.postValue(null);
                    }
                });
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData(){
        return userMutableLiveData;
    }
}
