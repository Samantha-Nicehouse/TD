package com.example.treasuredetector.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.treasuredetector.R;
import com.example.treasuredetector.model.Item;
import com.google.gson.Gson;


public class DetailFragment extends Fragment {

public  TextView  itemName;
 public TextView dateName;
 public ImageView categoryImage;
 public TextView geopointName;
 String itemJson;
  Item viewItem;
    public DetailFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_detail, container, false);
       itemName = (TextView) view.findViewById(R.id.category_tv);
       dateName = (TextView) view.findViewById(R.id.date_tv);
       categoryImage = (ImageView) view.findViewById(R.id.category_iv);
       geopointName = (TextView) view.findViewById(R.id.location_tv);

       if(getArguments() != null && getArguments().containsKey("itemObject"))
       {
           itemJson = getArguments().getString("itemObject");
           viewItem = new Gson().fromJson(itemJson,Item.class);
           itemName.setText(viewItem.getName());
           dateName.setText(viewItem.getCurrentDate());
           categoryImage.setImageResource(viewItem.getImageResource());


       }
       return view;
    }
}