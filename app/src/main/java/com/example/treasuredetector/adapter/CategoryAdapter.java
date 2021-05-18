package com.example.treasuredetector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.treasuredetector.R;
import com.example.treasuredetector.model.Category;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    Context context;
    List<Category> list;


    public CategoryAdapter(Context context, int resource, List<Category> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                @NotNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    public View getCustomView(int position, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_category_icon, parent, false);

        TextView textView = view.findViewById(R.id.textView);
        ImageView imageView = view.findViewById(R.id.imageView);

        textView.setText(list.get(position).getName());
        imageView.setImageResource(list.get(position).getIconResourceId());

        return view;
    }
}