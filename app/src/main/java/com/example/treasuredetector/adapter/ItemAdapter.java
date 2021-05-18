package com.example.treasuredetector.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.treasuredetector.R;
import com.example.treasuredetector.helper.Constants;
import com.example.treasuredetector.helper.Helper;
import com.example.treasuredetector.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * This is ItemAdapter it is used for populating recycler view in ItemActivity
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private List<Item> items = new ArrayList<>();
    private final ItemClickListener clickListener;
    Helper helper;

    public ItemAdapter(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
        helper = new Helper();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewDate;

        public ItemHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.imageViewItemImage);
            textViewTitle = view.findViewById(R.id.textViewItemTitle);
            textViewDescription = view.findViewById(R.id.textViewItemDescription);
            textViewDate = view.findViewById(R.id.textViewItemDate);

            view.setOnClickListener(v -> clickListener.onItemClick(items.get(getAbsoluteAdapterPosition()), v));
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

        holder.imageView.setImageResource(Constants.getResourceIdFromName(currentItem.getCategory()));
        holder.textViewTitle.setText(currentItem.getTitle());
        holder.textViewDescription.setText(currentItem.getDescription());
        holder.textViewDate.setText(helper.getFormattedDate(currentItem.getTime()));
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
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



