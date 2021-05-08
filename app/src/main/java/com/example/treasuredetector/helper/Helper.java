package com.example.treasuredetector.helper;

import android.content.Context;

import com.example.treasuredetector.R;

import java.util.HashMap;

public class Helper {

    private Context context;

    private HashMap<String, Integer> hashMap;

    public Helper(Context context) {
        this.context = context;
        hashMap = new HashMap<>();
        populateHashMap();
    }

    private void populateHashMap() {
        hashMap.put("Bottle Cap", R.drawable.ic_bottlecap);
        hashMap.put("Bow and Arrow", R.drawable.ic_bow_and_arrow);
        hashMap.put("Bullets", R.drawable.ic_bullets);
        hashMap.put("Coins", R.drawable.ic_coins);
        hashMap.put("Jewelry", R.drawable.ic_jewelry);
        hashMap.put("Key", R.drawable.ic_key);
        hashMap.put("Miscellaneous", R.drawable.ic_miscellaneous);
        hashMap.put("Quiver", R.drawable.ic_quiver);
        hashMap.put("Sword", R.drawable.ic_sword);
    }


    public int getResourceIdFromName(String name) {
        return hashMap.get(name);
    }

}
