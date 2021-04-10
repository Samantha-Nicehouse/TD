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

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment  {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    RecyclerView recyclerView;
    MyItemRecyclerViewAdapter adapter;
    FloatingActionButton fab;

    private static ArrayList<ItemModel> items = new ArrayList<>();

   static{     items.add(new ItemModel(R.drawable.ic_quiver, "Arrow", "Line 2"));
       items.add(new ItemModel(R.drawable.ic_bottlecap, "Bottlecap", "Line 2"));
       items.add(new ItemModel(R.drawable.ic_bow_and_arrow, "Bow&Arrow", "Line 2"));
       items.add(new ItemModel(R.drawable.ic_bullets, "Bullets","Line 2"));
       items.add(new ItemModel(R.drawable.ic_chest, "Miscellaneous", "Line 2"));
       items.add(new ItemModel(R.drawable.ic_coins, "Coins", "Line 2"));
       items.add(new ItemModel(R.drawable.ic_jewelry, "Jewelry", "Line 2"));
       items.add(new ItemModel(R.drawable.ic_key, "Keys", "Line 2"));
       items.add(new ItemModel(R.drawable.ic_sword, "Knife", "Line 2")); }
    /**}
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

       // TODO: Customize parameter initialization

    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


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