package com.example.treasuredetector.view;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.treasuredetector.R;
import com.example.treasuredetector.model.Item;

import java.util.List;

/**

 * TODO: Replace the implementation with code for your data type.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

   public List<Item> mValues;




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
  public RecyclerViewAdapter( ) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Item currentItem = mValues.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mIdView.setText(currentItem.getName());
        holder.mIdView2.setText(currentItem.getCurrentDate());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setList(List<Item> items)
    {
        this.mValues = items;
        notifyDataSetChanged();
    }

        @Override
        public String toString() {
            return super.toString() + " '" ;
        }
    }
