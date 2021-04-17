package com.example.treasuredetector.model;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ItemModel {

    private int mImageResource;
    private String mText1;
    public Calendar calendar = Calendar.getInstance();
    public String mCurrentDate;
    public ItemModel(int imageResource, String text1) {
        mImageResource = imageResource;
        mText1 = text1;
        mCurrentDate = DateFormat.getDateInstance().format(calendar.getTime());;
    }

    public int getImageResource() {
        return mImageResource;
    }
    public String getText1() {
        return mText1;
    }
    public String getCurrentDate() {
        return mCurrentDate;
    }


}

