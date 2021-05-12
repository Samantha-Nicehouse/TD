package com.example.treasuredetector.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.example.treasuredetector.R;

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


    private HashMap<String, Integer> hashMap;
    Context context;

    public Helper(Context context) {
        this.context = context;
        hashMap = new HashMap<>();
        populateHashMap();
    }

    public Helper() {
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


    public boolean isEmailValid(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public int getResourceIdFromName(String name) {
        return hashMap.get(name);
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

    public String getLatitude() {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    return String.valueOf(location.getLatitude());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return "0";
    }

    public String getLongitude() {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    return String.valueOf(location.getLongitude());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return "0";
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isLocationPermissionGrantedCheck() {
        String TAG = "Information Activity";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                Location location = getLastKnownLocation(context);
                return location != null;
            } else {
                Log.v(TAG, "Permission is revoked");
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    public boolean isLocationPermissionGranted() {
        String TAG = "Information Activity";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                Location location = getLastKnownLocation(context);
                if (location == null) {
                    askUserToTurnOnLocations(context);
                    return false;
                }
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 55);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    private void askUserToTurnOnLocations(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private Location getLastKnownLocation(Context context) {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public String saveToInternalStorage(Bitmap bitmapImage, String fileName) {
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
}
