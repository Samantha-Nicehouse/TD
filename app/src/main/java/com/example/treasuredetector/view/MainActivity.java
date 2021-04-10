package com.example.treasuredetector.view;

/*Activity is nothing but a java class in Android which has some
 pre-defined functions which are triggered
at different App states, which we can override to perform anything we want.*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.treasuredetector.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;



/*apart from controlling the app, Activity also controls creation,
destruction and other states of the App's lifecycle.*/
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnav);
        //pass the navigation listener from below
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    if (item.getItemId() == R.id.navBar_profile) {
                        selectedFragment = new SpinnerFragment();
                    } else if (item.getItemId() == R.id.navBar_map) {
                        selectedFragment = new MapFragment();
                    } else if (item.getItemId() == R.id.navBar_lists) {
                        selectedFragment = new ItemFragment();
                    } else {
                        selectedFragment = new HomeFragment();
                    }

                    //puts fragment in the framelayout
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

}