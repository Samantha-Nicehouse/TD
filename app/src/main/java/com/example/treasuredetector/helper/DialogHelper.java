package com.example.treasuredetector.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.example.treasuredetector.R;

public class DialogHelper {

    private final Dialog dialog;

    public DialogHelper(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void showDialog(){
        try {
            dialog.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dismissDialog(){
        try {
            dialog.dismiss();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
