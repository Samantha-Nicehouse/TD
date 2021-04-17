package com.example.treasuredetector.view;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.treasuredetector.R;
import com.example.treasuredetector.model.ItemModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

/**

 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

   public List<ItemModel> mValues;




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mIdView;
        public final TextView mIdView2;




        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.item_imageView);
            mIdView = (TextView) view.findViewById(R.id.item_name);
            mIdView2 = (TextView) view.findViewById(R.id.item_line);


        }
    }
    public MyItemRecyclerViewAdapter(List<ItemModel> items ) {
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ItemModel currentItem = mValues.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mIdView.setText(currentItem.getText1());
        holder.mIdView2.setText(currentItem.mCurrentDate);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



        @Override
        public String toString() {
            return super.toString() + " '" ;
        }
    }
