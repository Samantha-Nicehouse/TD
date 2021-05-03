package com.example.treasuredetector.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.treasuredetector.R;
import com.example.treasuredetector.model.Item;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private List<Item> items = new ArrayList<>();
    private OnItemClickListener listener;


    public ItemAdapter() {}



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

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    class ItemHolder extends RecyclerView.ViewHolder  {
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

