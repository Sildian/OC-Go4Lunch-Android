package com.sildian.go4lunch.controller.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**************************************************************************************************
 * BaseFragment
 * Base for all fragments attached to MainActivity
 * Manages the user's location
 *************************************************************************************************/

public abstract class BaseFragment extends Fragment {

    /**Request keys**/

    private static final int KEY_REQUEST_PERMISSION=201;

    /**Location permission**/

    private static final String PERMISSION_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;

    /**Location management**/

    protected FusedLocationProviderClient fusedLocationProviderClient;      //Allows to get the location
    protected LatLng userLocation;                                          //The user's location

    /**Abstract methods**/

    protected abstract void onUserLocationReceived();                       //Handles the user's location result

    /**Callbacks**/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context);
    }

    /**Gets the user's location**/

    protected void updateUserLocation() {

        /*Checks if the location research can begin*/

        if (getActivity() != null && this.fusedLocationProviderClient!=null) {
            if (Build.VERSION.SDK_INT<23||getActivity().checkSelfPermission(PERMISSION_LOCATION)==PackageManager.PERMISSION_GRANTED) {

                /*Begins the location research*/

                this.fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    onUserLocationReceived();
                                } else {
                                    //TODO handle
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //TODO handle
                            }
                        });

            }else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), PERMISSION_LOCATION)) {
                //TODO handle
            }else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{PERMISSION_LOCATION}, KEY_REQUEST_PERMISSION);
                //TODO handle result
            }
        }else{
            //TODO handle
        }
    }
}
