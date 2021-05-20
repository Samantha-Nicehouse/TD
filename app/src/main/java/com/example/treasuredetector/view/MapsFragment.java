package com.example.treasuredetector.view;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.treasuredetector.R;
import com.example.treasuredetector.model.Item;
import com.example.treasuredetector.repository.ItemRepository;
import com.example.treasuredetector.view_model.ItemViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;

public class MapsFragment extends Fragment {

    private static final String TAG = "MapsFragment";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;
    private GoogleMap mMap;

    private Boolean mLocationPermissionGranted = false;
    private static final float DEFAULT_ZOOM = 12f;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    ItemViewModel itemViewModel;


    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Aarhus, DK
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        /*some of this code was autogenerated by FragmentMap selection*/
        @Override
        public void onMapReady(GoogleMap googleMap) {
//            Toast.makeText(getContext(), "Map is Ready", LENGTH_SHORT).show();
            Log.d(TAG, "onMapReady: Map is Ready");
            //when map is loaded
            mMap = googleMap;

           // itemViewModel = new ViewModelProvider(getActivity()).get(ItemViewModel.class);
            itemViewModel.getLastFiveEntries().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
                @Override
                public void onChanged(List<Item> items) {

                    moveCamera(items);

                }
            });

        }
    };


    /*this code was autogenerated by FragmentMap selection*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    /*this code was autogenerated by FragmentMap selection*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                itemViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ItemViewModel.class);
                SupportMapFragment mapFragment =
                        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
               mapFragment.onCreate(savedInstanceState);
                mapFragment.onResume();
                mapFragment.onAttach(getActivity());
                mapFragment.getMapAsync(callback);

            } else {
                ActivityCompat.requestPermissions(getActivity(), permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext().getApplicationContext());
        try {
            if (mLocationPermissionGranted) {
                @SuppressLint("MissingPermission") Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Found location");
                            Toast.makeText(getContext(), "found current location", LENGTH_LONG).show();
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
//                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
//                                        DEFAULT_ZOOM, "Updated");
                            }
                        } else {
                            Log.d(TAG, "onComplete: Current location is null");
                            Toast.makeText(getContext(), " unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    private void moveCamera(List<Item> itemList) {
        builder = new LatLngBounds.Builder();
        if(itemList != null && itemList.size() > 0){
        //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(itemList.get(0).getLatitude(), itemList.get(0).getLongitude()), DEFAULT_ZOOM));
           MarkerOptions options = new MarkerOptions();

            for (Item item: itemList) {

                options.position(new LatLng(item.getLatitude(), item.getLongitude()));
                options.title(item.getTitle());
                options.snippet(item.getDescription());
                mMap.addMarker(options);
                builder.include(options.getPosition());
            }
        }
       bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
       int padding = (int) (width * 0.30);
      CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height,padding);
       mMap.animateCamera(cu);
    }
}




















