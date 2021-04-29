package com.example.treasuredetector.view;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.treasuredetector.R;

public class SpinnerActivity extends AppCompatActivity {

    Spinner trSpinner;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_spinner_item);

        trSpinner = (Spinner) findViewById(R.id.trSpinner);

    }
}
