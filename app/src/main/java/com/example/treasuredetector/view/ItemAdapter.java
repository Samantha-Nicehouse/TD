package com.example.treasuredetector.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.treasuredetector.R;
import com.example.treasuredetector.model.Item;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.bottomnavigation.OnItemClickListener;

/**

 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private List<Item> items = new ArrayList<>();
    private ItemClickListener clickListener;


    public ItemAdapter() {}

    public ItemAdapter(List<Item> items, ItemClickListener clickListener) {
        this.items = items;
        this.clickListener = clickListener;

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
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mName.setText(currentItem.getName());
        holder.mDate.setText(currentItem.getCurrentDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickListener.onItemClick(currentItem);
            }
        }

       );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setList(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public Item getItemAt(int position) {
        return items.get(position);
    }

    public interface ItemClickListener{
        void onItemClick(Item item);
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mName;
        public final TextView mDate;

        public ItemHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.item_imageView);
            mName = (TextView) view.findViewById(R.id.item_name);
            mDate = (TextView) view.findViewById(R.id.item_line);


        }
    }
}

