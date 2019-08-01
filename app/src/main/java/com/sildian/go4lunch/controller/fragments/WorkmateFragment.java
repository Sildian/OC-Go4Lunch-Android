package com.sildian.go4lunch.controller.fragments;


import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.view.WorkmateAdapter;

import java.util.List;

import butterknife.BindView;

/**************************************************************************************************
 * WorkmateFragment
 * Shows the list of all workmates
 *************************************************************************************************/

/*public class WorkmateFragment extends BaseFragment {

    /**UI Components**/

    /*@BindView(R.id.fragment_workmate_workmates) RecyclerView workmatesView;
    private WorkmateAdapter workmateAdapter;

    public WorkmateFragment(PlacesClient placesClient, LatLng userLocation, Workmate currentUser, List<Restaurant> restaurants) {
        super(placesClient, userLocation, currentUser, restaurants);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_workmate, container, false);
    }

}*/
