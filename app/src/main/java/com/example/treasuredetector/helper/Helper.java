package com.example.treasuredetector.helper;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.LOCATION_SERVICE;

public class Helper {

    public static final int LOCATION_PERMISSION = 1234;
    private Context context;

    public Helper(Context context) {
        this.context = context;
    }

    public Helper() {
    }

    public boolean isEmailInvalid(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return !matcher.find();
    }

    public String getFormattedDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));

        StringBuilder formattedDate = new StringBuilder();
        formattedDate.append(calendar.get(Calendar.DATE) < 10 ? "0" + calendar.get(Calendar.DATE) : calendar.get(Calendar.DATE));
        formattedDate.append("/");
        formattedDate.append((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : (calendar.get(Calendar.MONTH) + 1));
        formattedDate.append("/");
        formattedDate.append(calendar.get(Calendar.YEAR));
        formattedDate.append(" at ");
        formattedDate.append(calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : calendar.get(Calendar.HOUR_OF_DAY));
        formattedDate.append(":");
        formattedDate.append(calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE));

        return formattedDate.toString();
    }

    public String saveImageToStorage(Bitmap bitmapImage, String fileName) {
        ContextWrapper contextWrapper = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = contextWrapper.getDir("TreasureDetector", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath = new File(directory, fileName + ".jpg");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 50, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public Bitmap getImageFromStorage(String path, String fileName) {

        try {
            File file = new File(path, fileName + ".jpg");
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteImageFromStorage(String path, String fileName) {
        try {
            File file = new File(path, fileName + ".jpg");
            return file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isLocationPermissionGranted(Activity activity) {
        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return isLocationServiceOn();
            } else {
                ActivityCompat.requestPermissions(activity, permission, LOCATION_PERMISSION);
                return false;
            }
        } else {
            ActivityCompat.requestPermissions(activity, permission, LOCATION_PERMISSION);
            return false;
        }
    }

    private boolean isLocationServiceOn() {
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                // Show location settings when the user acknowledges the alert dialog
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            return false;
        }
        return true;
    }

}
