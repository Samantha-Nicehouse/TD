package com.example.treasuredetector.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.treasuredetector.R;
import com.example.treasuredetector.model.Item;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.treasuredetector.R.mipmap.coins;

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
        }else {

            holder.mDate.setText(currentItem.getCurrentDate());
        }
        if(currentItem.getImageURI().equals(""))
        {
            holder.mItemImage.setImageURI(Uri.parse("android.resource://com.example.treasuredetector/mipmap/ic_launcher.jpg"));
        }else{
            Glide.with(holder.mItemImage).load(currentItem.getImageURI()).into(holder.mItemImage);
        }


    }

    public void setItems(List<Item> items)
    {
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

    public interface ItemClickListener{
        void onItemClick(Item item, View view);
    }

    class ItemHolder extends RecyclerView.ViewHolder  {

        public final ImageView mImageView;
        public final TextView mName;
        public final TextView mDate;
        public final ImageView mItemImage;



        public ItemHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.item_imageView);
            mName = (TextView) view.findViewById(R.id.item_name);
            mDate = (TextView) view.findViewById(R.id.item_line);
            mItemImage = (ImageView) view.findViewById(R.id.detail_image);


           view.setOnClickListener(v-> {
               clickListener.onItemClick(items.get(getAbsoluteAdapterPosition()),v);
           });
        }


    }

    }



