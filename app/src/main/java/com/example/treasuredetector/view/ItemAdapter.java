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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**

 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private List<Item> items = new ArrayList<>();
    private ItemClickListener clickListener;


    public ItemAdapter(){}

    public ItemAdapter(ItemClickListener itemClickListener){
        this.clickListener  = itemClickListener;
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
        if(currentItem.getCurrentDate().equals(""))
        {

            currentItem.setCurrentDate(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
        }

        holder.mDate.setText(currentItem.getCurrentDate());


    }

    public void setItems(List<Item> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setList(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ItemClickListener{
        void onItemClick(Item item, View view);
    }

    class ItemHolder extends RecyclerView.ViewHolder  {

        public final ImageView mImageView;
        public final TextView mName;
        public final TextView mDate;

        public ItemHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.item_imageView);
            mName = (TextView) view.findViewById(R.id.item_name);
            mDate = (TextView) view.findViewById(R.id.item_line);

           view.setOnClickListener(v-> {
               clickListener.onItemClick(items.get(getAbsoluteAdapterPosition()),v);
           });
        }


    }

    }



