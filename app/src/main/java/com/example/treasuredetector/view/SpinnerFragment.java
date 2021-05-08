package com.example.treasuredetector.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.treasuredetector.R;

import android.widget.AdapterView.OnItemSelectedListener;
import java.util.ArrayList;
import java.util.List;

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
                            //do nothing
                } else {
                    //on selecting the a spinner item
                    String item = parent.getItemAtPosition(position).toString();

                    //show selected spinner item
                    Toast toast = Toast.makeText(parent.getContext(), "Selected" + item, Toast.LENGTH_LONG);
                            toast.show();

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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}







