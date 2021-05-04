package com.example.treasuredetector.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.treasuredetector.ItemRepository;
import com.example.treasuredetector.R;

import com.example.treasuredetector.model.Geopoint;
import com.example.treasuredetector.model.Item;
import com.example.treasuredetector.view_model.GeopointViewModel;
import com.example.treasuredetector.view_model.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment  {
    private ItemViewModel itemViewModel;
    private GeopointViewModel geopointViewModel;
    RecyclerView recyclerView;
    ItemAdapter adapter;
    FloatingActionButton fab;


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
        itemViewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                adapter.setItems(items);
            }
        });
    /*    //only returns if activity is running in the foreground
        itemViewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                adapter.setList(items);
                //update recyclerView
                Toast.makeText(getContext().getApplicationContext(), "onChanged", Toast.LENGTH_SHORT).show();
            }
        });*/
        //adapter.setItemClickListener((v,);
        recyclerView = view.findViewById(R.id.list);
        buildItemListData();
       BuildRecyclerView();

        return view;

    }



    private void BuildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(new ItemAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Item item, View view) {
                Bundle bundle = new Bundle();
                bundle.putString("itemObject",new Gson().toJson(item));
                Navigation.findNavController(view).navigate(R.id.detailFragment,bundle);
            }
        });
        //set the adapter
        recyclerView.setAdapter(adapter);
    }


   private void buildItemListData(){
     itemViewModel.insert(new Item(R.drawable.ic_bullets, "Bullet", ""));
    itemViewModel.insert(new Item(R.drawable.ic_key, "Key", ""));
      itemViewModel.insert(new Item(R.drawable.ic_sword, "Sword", ""));
       itemViewModel.insert(new Item(R.drawable.ic_quiver, "Quiver", ""));
      itemViewModel.insert(new Item(R.drawable.ic_bullets, "Bullet", "May 1, 2020"));
       itemViewModel.insert(new Item(R.drawable.ic_bullets, "Bullet", "April 20, 1986"));
       itemViewModel.insert(new Item(R.drawable.ic_bullets, "Bullet", "July 4, 1992"));


   }

   /* @Override
    public void onItemClick(Item item) {
        Fragment fragment = ItemViewFragment.newInstance(item.getName());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, ItemViewFragment.class, null);
       // transaction.hide(getActivity().getSupportFragmentManager().findFragmentById(R.id.itemViewFragment));
       // transaction.add(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/
/*
    @Override
    public void onListItemClick(int position) {
        Fragment fragment = ItemViewFragment.newInstance(items.get(position).getName());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, ItemViewFragment.class, null);
        // transaction.hide(getActivity().getSupportFragmentManager().findFragmentById(R.id.itemViewFragment));
        // transaction.add(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/
}