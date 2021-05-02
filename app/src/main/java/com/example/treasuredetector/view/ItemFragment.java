package com.example.treasuredetector.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
public class ItemFragment extends Fragment implements ItemAdapter.ItemClickListener {
    private ItemViewModel itemViewModel;
    RecyclerView recyclerView;
    ItemAdapter adapter;
    FloatingActionButton fab;
    private List<Item> items = new ArrayList<>();

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
        itemViewModel.getAllItems().observe(getViewLifecycleOwner(), items -> adapter.setList(items));
        //update recyclerView
        Toast.makeText(getContext().getApplicationContext(), "onChanged", Toast.LENGTH_SHORT).show();
        recyclerView = view.findViewById(R.id.list);
       BuildRecyclerView();
        return view;

    }



    private void BuildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter();
        //set the adapter
        recyclerView.setAdapter(adapter);
    }

    private void InitRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        ItemAdapter adapter = new ItemAdapter();
        //set the adapter
        recyclerView.setAdapter(adapter);

    }

   private void buildItemListData(){
       itemViewModel.insert(new Item(R.drawable.ic_bullets, "Bullet"));
       itemViewModel.insert(new Item(R.drawable.ic_key, "Key"));
       itemViewModel.insert(new Item(R.drawable.ic_sword, "Sword"));
       itemViewModel.insert(new Item(R.drawable.ic_quiver, "Quiver"));
       itemViewModel.insert(new Item(R.drawable.ic_bullets, "Bullet"));
       itemViewModel.insert(new Item(R.drawable.ic_bullets, "Bullet"));
       itemViewModel.insert(new Item(R.drawable.ic_bullets, "Bullet"));
   }

    @Override
    public void onItemClick(Item item) {
        Fragment fragment = ItemViewFragment.newInstance(item.getName());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        //transaction.replace(R.id.fragment_container, fragment, "itemView_fragment");
        transaction.hide(getActivity().getSupportFragmentManager().findFragmentById(R.id.itemViewFragment));
        transaction.add(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}