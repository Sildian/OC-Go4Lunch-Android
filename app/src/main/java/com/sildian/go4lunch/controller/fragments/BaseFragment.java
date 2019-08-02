package com.sildian.go4lunch.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;

import java.util.List;

import butterknife.ButterKnife;

/**************************************************************************************************
 * BaseFragment
 * Base for all fragments attached to MainActivity
 *************************************************************************************************/

public abstract class BaseFragment extends Fragment {

    /**Information**/

    protected PlacesClient placesClient;                                        //The placesClient allowing to use Google Places API
    protected LatLng userLocation;                                              //The user's location
    protected Workmate currentUser;                                             //The current user
    protected List<Restaurant> restaurants;                                     //The list of restaurants in the area

    /**Abstract methods**/

    protected abstract int getFragmentLayout();                                 //Gets the fragment layout
    protected abstract void initializeView(Bundle SavedInstanceState);          //Initializes the view
    public abstract void onUserLocationReceived(LatLng userLocation);           //Handles the user's location result
    public abstract void onRestaurantsReceived(List<Restaurant> restaurants);   //Handles the restaurants result

    /**Constructor**/

    public BaseFragment(PlacesClient placesClient, LatLng userLocation, Workmate currentUser, List<Restaurant> restaurants) {
        this.placesClient=placesClient;
        this.userLocation=userLocation;
        this.currentUser=currentUser;
        this.restaurants=restaurants;
    }

    /**Callbacks**/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getFragmentLayout(), container, false);
        ButterKnife.bind(this, view);
        initializeView(savedInstanceState);
        return view;
    }

    /**Updates the current user**/

    public void updateCurrentUser(Workmate workmate){
        this.currentUser=workmate;
    }
}
