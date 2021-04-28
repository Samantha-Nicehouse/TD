package com.example.treasuredetector.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.treasuredetector.R;

import com.example.treasuredetector.model.Item;
import com.example.treasuredetector.view_model.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment implements ItemAdapter.ItemClickListener  {
    private ItemViewModel itemViewModel;
    RecyclerView recyclerView;
    ItemAdapter adapter;
    FloatingActionButton fab;
    private List<Item> list = new ArrayList<>();

    /**}
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        //pass the fragment so that viewmodel knows which lifecycle to scope to
        //android activity will destroy the fragment when "this" is finished



        itemViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ItemViewModel.class);

        //only returns if activity is running in the foreground
        itemViewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                adapter.setList(items);
            }
        });

        recyclerView = view.findViewById(R.id.list);
        BuildRecyclerView();
        return view;

    }





    private void BuildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(list, this);
        //set the adapter
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(Item item, View view) {

    }
}