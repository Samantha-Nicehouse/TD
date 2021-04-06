package com.example.treasuredetector.model;
import com.example.treasuredetector.R;

import java.util.List;

public class Item2 {
   public String itemName;
   public int drawable;
    private static List<String> treasureNameList;

  public static String[] nameArray = {"Arrow","Bottlecap","Bullet","Coin", "Jewelry", "Keys","Knife", "Misc","Weapon"};
   // static String[] versionArray = {"1.5", "1.6", "2.0-2.1", "2.2-2.2.3", "2.3-2.3.7", "3.0-3.2.6", "4.0-4.0.4", "4.1-4.3.1", "4.4-4.4.4", "5.0-5.1.1","6.0-6.0.1"};

 public static Integer[] drawableArray = {R.drawable.ic_quiver, R.drawable.ic_bottlecap, R.drawable.ic_coins,
            R.drawable.ic_jewelry, R.drawable.ic_key, R.drawable.ic_sword, R.drawable.ic_chest,
            R.drawable.ic_bow_and_arrow};
 public static Integer[] id = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};



    public static List<String> allTreasureNames() {
        for (int i = 1; i < Item2.nameArray.length; i++) {
            treasureNameList.add(nameArray[i]);
        }
        return treasureNameList;
    }


public String getItemName(){
        return itemName;
}
    public int getDrawable()
    {
        return drawable;
    }
    //  static Integer[] id_ = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
}




