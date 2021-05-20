package com.example.treasuredetector.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.treasuredetector.ItemActivity;
import com.example.treasuredetector.R;

import com.example.treasuredetector.adapter.ItemAdapter;
import com.example.treasuredetector.helper.Helper;
import com.example.treasuredetector.view_model.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {
    RecyclerView recyclerView;
    ItemAdapter adapter;
    FloatingActionButton fab;
    Helper helper;

    /**
     * }
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        helper = new Helper(getActivity());

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ItemActivity.class);
            intent.putExtra("flow","addItem");
            startActivity(intent);
        });
        //pass the fragment so that viewmodel knows which lifecycle to scope to
        //android activity will destroy the fragment when "this" is finished

        ItemViewModel itemViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items);
            adapter.notifyDataSetChanged();

        });

        recyclerView = view.findViewById(R.id.list);
        BuildRecyclerView();
        helper.isLocationPermissionGranted(getActivity());
        return view;
    }

    private void BuildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter((item, view) -> {
            Intent intent = new Intent(getContext(), ItemActivity.class);
            intent.putExtra("flow","viewItem");
            intent.putExtra("data", item);
            try {
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        //set the adapter
        recyclerView.setAdapter(adapter);
    }

}