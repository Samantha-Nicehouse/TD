package com.example.treasuredetector.view;

/*Activity is nothing but a java class in Android which has some
 pre-defined functions which are triggered
at different App states, which we can override to perform anything we want.*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.treasuredetector.MapsFragment;
import com.example.treasuredetector.R;

import com.example.treasuredetector.view_model.ItemViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;



/*apart from controlling the app, Activity also controls creation,
destruction and other states of the App's lifecycle.*/
public class MainActivity extends AppCompatActivity {

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