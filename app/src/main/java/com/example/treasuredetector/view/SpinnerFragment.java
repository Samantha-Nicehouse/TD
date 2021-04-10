package com.example.treasuredetector.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treasuredetector.model.ItemModel;
import com.example.treasuredetector.view.MyItemRecyclerViewAdapter;

import com.example.treasuredetector.R;
import com.example.treasuredetector.view.MyItemRecyclerViewAdapter.ViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_spinner_item;
import static android.widget.SpinnerAdapter.*;

/**

 * create an instance of this fragment.
 */
public class SpinnerFragment extends Fragment implements OnItemSelectedListener {

    Spinner spinner;

    public SpinnerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.simple_spinner_dropdown_item, container, false);

        spinner = (Spinner) view.findViewById(R.id.trSpinner);

        List<String> categories = new ArrayList<>();
        categories.add(0, "Choose category");
        categories.add("Bullet");
        categories.add("Coins");
        categories.add("Jewelry");
        categories.add("Junk");
        categories.add("Key");
        categories.add("Knife");
        categories.add("Misc");


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter(view.getContext(),
                android.R.layout.simple_spinner_item, categories);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attach item to adapter in spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose category")) {

                } else {
                    //on selecting the a spinner item
                    String item = parent.getItemAtPosition(position).toString();

                    //show selected spinner item
                    Toast.makeText(parent.getContext(), "Selected" + item, Toast.LENGTH_SHORT).show();

                    //anything else I can make here
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }

        });

        return view;


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).equals("Choose category")) {

        } else {
            //on selecting the a spinner item
            String item = parent.getItemAtPosition(position).toString();

            //show selected spinner item
            Toast.makeText(parent.getContext(), "Selected" + item, Toast.LENGTH_SHORT).show();

            //anything else I can make here
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}







