package com.example.treasuredetector.helper;

import com.example.treasuredetector.R;
import com.example.treasuredetector.model.Category;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static List<Category> getCategoryIconList() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("Select", R.drawable.ic_baseline_arrow_drop_down_24));
        list.add(new Category("Bottle Cap", R.drawable.ic_bottlecap));
        list.add(new Category("Bow and Arrow", R.drawable.ic_bow_and_arrow));
        list.add(new Category("Bullets", R.drawable.ic_bullets));
        list.add(new Category("Coins", R.drawable.ic_coins));
        list.add(new Category("Jewelry", R.drawable.ic_jewelry));
        list.add(new Category("Key", R.drawable.ic_key));
        list.add(new Category("Miscellaneous", R.drawable.ic_miscellaneous));
        list.add(new Category("Quiver", R.drawable.ic_quiver));
        list.add(new Category("Sword", R.drawable.ic_sword));
        return list;
    }

    public static int getResourceIdFromName(String name) {
        switch (name) {
            case "Bottle Cap":
                return R.drawable.ic_bottlecap;
            case "Bow and Arrow":
                return R.drawable.ic_bow_and_arrow;
            case "Bullets":
                return R.drawable.ic_bullets;
            case "Coins":
                return R.drawable.ic_coins;
            case "Jewelry":
                return R.drawable.ic_jewelry;
            case "Key":
                return R.drawable.ic_key;
            case "Quiver":
                return R.drawable.ic_quiver;
            case "Sword":
                return R.drawable.ic_sword;
            default:
                return R.drawable.ic_miscellaneous;
        }
    }
}
