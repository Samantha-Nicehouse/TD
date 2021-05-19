package com.example.treasuredetector.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import com.example.treasuredetector.ItemActivity;

public class LocationHelper {

    private final LocationManager locationManagerNetwork;//uses wifi for location
    private final LocationManager locationManagerGPS;//uses simcard for location
    private final LocationListener locationListener;
    private final Context context;
    private OnLocationChange onLocationChange;

    public LocationHelper(Context context) {
        this.context = context;

        locationManagerNetwork = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        locationManagerGPS = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new MyLocationListener();
    }

    public void setOnLocationChange(OnLocationChange onLocationChange) {
        this.onLocationChange = onLocationChange;
    }

    public void startLocationChangeListener() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManagerNetwork.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, Integer.MAX_VALUE, 1, locationListener); //gives updates by this provider at max interval within  a distance of 1

        locationManagerGPS.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, Integer.MAX_VALUE, 1, locationListener);
    }

    public void removeLocationChangeListener(){
        try {
            locationManagerNetwork.removeUpdates(locationListener);
            locationManagerGPS.removeUpdates(locationListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*---------- Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) { // when if first attached to the manager, gets currentlocation and then listens for changed location
            onLocationChange.onLocationChanged(location);
        }
    }

    public interface OnLocationChange{
        void onLocationChanged(Location location);
    }

}
