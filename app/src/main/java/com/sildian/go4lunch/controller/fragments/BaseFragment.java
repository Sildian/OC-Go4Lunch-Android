package com.sildian.go4lunch.controller.fragments;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.sildian.go4lunch.model.Restaurant;

import java.util.List;

/**************************************************************************************************
 * BaseFragment
 * Base for all fragments attached to MainActivity
 *************************************************************************************************/

public abstract class BaseFragment extends Fragment {

    protected LatLng userLocation;                                              //The user's location
    protected List<Restaurant> restaurants;                                     //The list of restaurants in the area

    /**Abstract methods**/

    public abstract void onUserLocationReceived(LatLng userLocation);           //Handles the user's location result
    public abstract void onRestaurantsReceived(List<Restaurant> restaurants);   //Handles the restaurants result

    /**Constructor**/

    public BaseFragment(LatLng userLocation, List<Restaurant> restaurants) {
        this.userLocation=userLocation;
        this.restaurants=restaurants;
    }
}
