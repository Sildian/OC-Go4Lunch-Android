package com.sildian.go4lunch.controller.fragments;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

/**************************************************************************************************
 * BaseFragment
 * Base for all fragments attached to MainActivity
 * Manages the user's location
 *************************************************************************************************/

public abstract class BaseFragment extends Fragment {

    protected LatLng userLocation;                                              //The user's location

    /**Abstract methods**/

    public abstract void onUserLocationReceived(LatLng userLocation);           //Handles the user's location result

    /**Constructor**/

    public BaseFragment(LatLng userLocation) {
        this.userLocation=userLocation;
    }
}
