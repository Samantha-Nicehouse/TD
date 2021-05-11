package com.example.treasuredetector.repository;

import android.app.Application;

import com.example.treasuredetector.dao.UserDao;
import com.example.treasuredetector.database.TreasureDetectorDatabase;
import com.example.treasuredetector.model.User;


public class UserRepository {

    private final UserDao userDao;

    public UserRepository(Application application) {

        TreasureDetectorDatabase database = TreasureDetectorDatabase.getInstance(application);
        userDao = database.userDao();
    }

    public long insert(User user) {

        try {
            return userDao.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public User login(String username, String password) {

        try {
            return userDao.login(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
