package com.example.treasuredetector.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.treasuredetector.R;

import com.example.treasuredetector.model.ItemModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment  {

    RecyclerView recyclerView;
    MyItemRecyclerViewAdapter adapter;
    FloatingActionButton fab;


    private static ArrayList<ItemModel> items = new ArrayList<>();

   static{     items.add(new ItemModel(R.drawable.ic_quiver, "Arrow"));
       items.add(new ItemModel(R.drawable.ic_bottlecap, "Bottlecap"));
       items.add(new ItemModel(R.drawable.ic_bow_and_arrow, "Bow&Arrow"));
       items.add(new ItemModel(R.drawable.ic_bullets, "Bullets"));
       items.add(new ItemModel(R.drawable.ic_chest, "Miscellaneous"));
       items.add(new ItemModel(R.drawable.ic_coins, "Coins"));
       items.add(new ItemModel(R.drawable.ic_jewelry, "Jewelry"));
       items.add(new ItemModel(R.drawable.ic_key, "Keys"));
       items.add(new ItemModel(R.drawable.ic_sword, "Knife")); }
    /**}
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new MyItemRecyclerViewAdapter((items));
        recyclerView.setAdapter(adapter);
        return view;


    }

}