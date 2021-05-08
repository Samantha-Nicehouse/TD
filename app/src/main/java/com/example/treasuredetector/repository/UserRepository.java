package com.example.treasuredetector.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.treasuredetector.dao.UserDao;
import com.example.treasuredetector.database.TreasureDetectorDatabase;
import com.example.treasuredetector.model.User;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepository {

    private static final String TAG = "UserRepository";
    private UserDao userDao;
    private long insertedItemId;
    private User loggedInUser = null;
    private int loginStatus = 0; // 0 mean started, 1 mean successful & 2 mean unsuccessful

    public UserRepository() {
    }

    public UserRepository(Application application) {

        TreasureDetectorDatabase database = TreasureDetectorDatabase.getInstance(application);
        userDao = database.userDao();

    }

    public long insert(User user) {
        insertedItemId = -2;
        insertUserInBackground(user);
        while (insertedItemId == -2) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return insertedItemId;
    }

    public User login(String username, String password) {
        loginStatus = 0;
        loginUserInBackground(username, password);
        while (loginStatus == 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (loginStatus == 1)
            return loggedInUser;
        else
            return null;
    }


    private void loginUserInBackground(String username, String password) {

        Observable
                .create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<User> emitter) {

                        try {
                            loggedInUser = userDao.login(username, password);
                            loginStatus = 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                            loginStatus = 2;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        Log.d(TAG, "login user onSubscribe called");
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull User user) {
                        Log.d(TAG, "login user onNext called");
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.d(TAG, "login user onError called");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "login user onComplete called");
                    }
                });

    }

    private void insertUserInBackground(User user) {

        Observable
                .create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<User> emitter) {

                        try {
                            insertedItemId = userDao.insert(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                            insertedItemId = -1;
                        }
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "insert user onSubscribe called");
                    }

                    @Override
                    public void onNext(@NonNull User User) {
                        Log.d(TAG, "insert user onNext called");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "insert user onError called");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "insert user onComplete called");
                    }
                });
    }
}
