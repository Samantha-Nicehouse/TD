package com.example.treasuredetector.repository;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class UserAuthRepository {


    private static final String TAG = "UserAuthRepository";
    private final FirebaseAuth mAuth;
    CallbackRegister callbackRegister;
    CallbackLogin callbackLogin;

    public UserAuthRepository() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void registerUser(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");

                        //This assign the name to this newly registered user
                        new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                        callbackRegister.registerSuccessful();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        callbackRegister.registerFailed(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                    }
                })
                .addOnCanceledListener(() -> {
                    Log.d(TAG, "onCanceled: ");
                    callbackRegister.registerFailed("canceled");
                });
    }

    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        callbackLogin.loginSuccessful();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        callbackLogin.loginFailed(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                    }
                })
                .addOnCanceledListener(() -> {
                    Log.d(TAG, "onCancelled: ");
                    callbackLogin.loginFailed("cancelled");
                });
    }

    public void setCallbackRegister(CallbackRegister callbackRegister) {
        this.callbackRegister = callbackRegister;
    }

    public interface CallbackRegister {
        void registerSuccessful();

        void registerFailed(String msg);
    }

    public void setCallbackLogin(CallbackLogin callbackLogin){
        this.callbackLogin = callbackLogin;
    }

    public interface CallbackLogin {
        void loginSuccessful();

        void loginFailed(String msg);
    }
}
