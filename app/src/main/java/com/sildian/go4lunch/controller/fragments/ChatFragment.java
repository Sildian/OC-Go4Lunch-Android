package com.sildian.go4lunch.controller.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;

import java.util.List;

/**************************************************************************************************
 * ChatFragment
 * Allow the workmates to chat together
 *************************************************************************************************/

public class ChatFragment extends BaseFragment {

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    /*********************************************************************************************
     * Constructors
     ********************************************************************************************/

    public ChatFragment(){
        super();
    }

    public ChatFragment(PlacesClient placesClient, LatLng userLocation, Workmate currentUser, List<Restaurant> restaurants) {
        super(placesClient, userLocation, currentUser, restaurants);
    }

    /*********************************************************************************************
     * BaseFragment methods
     ********************************************************************************************/

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initializeView(Bundle SavedInstanceState) {

    }

    @Override
    public void onUserLocationReceived(LatLng userLocation) {

    }

    @Override
    public void onRestaurantsReceived(List<Restaurant> restaurants) {

    }
}
