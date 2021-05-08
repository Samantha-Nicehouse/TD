package com.example.treasuredetector;

/*Activity is nothing but a java class in Android which has some
 pre-defined functions which are triggered
at different App states, which we can override to perform anything we want.*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.example.treasuredetector.R;

import com.example.treasuredetector.model.User;
import com.example.treasuredetector.view_model.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


/*apart from controlling the app, Activity also controls creation,
destruction and other states of the App's lifecycle.*/
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TreasureDetectorMainActivity";

    NavController navController;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupNavigation();
    }


    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomnav);

    }

    private void setupNavigation()
    {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }
}