package com.example.treasuredetector.model;
import androidx.room.Entity;

import com.example.treasuredetector.R;

import java.util.List;



@Entity(tableName= "item_table")
public class Item {
   public String itemName;
   public int drawable;
    private static List<String> treasureNameList;

  public static String[] nameArray = {"Bullet","Coins", "Jewelry","Junk", "Keys","Knife","Misc"};

 public static Integer[] drawableArray = {R.drawable.ic_bullets, R.drawable.ic_coins,
            R.drawable.ic_jewelry, R.drawable.ic_bottlecap, R.drawable.ic_key, R.drawable.ic_sword, R.drawable.ic_chest,
         };
 public static String[] id = { "1", "2","3","4","5","6","7","8","9"};



    public static List<String> allTreasureNames() {
        for (int i = 1; i < Item.nameArray.length; i++) {
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

}




