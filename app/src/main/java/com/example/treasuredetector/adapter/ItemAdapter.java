package com.example.treasuredetector.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.treasuredetector.R;
import com.example.treasuredetector.model.Item;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private List<Item> items = new ArrayList<>();
    private ItemClickListener clickListener;


    public ItemAdapter() {
    }

    public ItemAdapter(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewDate;

        public ItemHolder(View view) {
            super(view);

            view.setOnClickListener(v -> {
                clickListener.onItemClick(items.get(getAbsoluteAdapterPosition()), v);
            });
        }
    }


    @NonNull
    @Override
    public ItemAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ItemHolder(view);
    }


    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {
        Item currentItem = items.get(position);


    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();//changes to detail view
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setList(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClick(Item item, View view);
    }



}



