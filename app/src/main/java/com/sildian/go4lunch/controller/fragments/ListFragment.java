package com.sildian.go4lunch.controller.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.view.RestaurantAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**************************************************************************************************
 * ListFragment
 * Shows the list of restaurants found in the area and its related information
 *************************************************************************************************/

public class ListFragment extends BaseFragment {

    /**UI components**/

    @BindView(R.id.fragment_list_restaurants) RecyclerView restaurantsView;
    private RestaurantAdapter restaurantAdapter;

    /**Constructor**/

    public ListFragment(PlacesClient placesClient, LatLng userLocation, List<Restaurant> restaurants) {
        super(placesClient, userLocation, restaurants);
    }

    /**Callbacks**/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        initializeRestaurantsView();
        return view;
    }

    @Override
    public void onUserLocationReceived(LatLng userLocation) {

    }

    @Override
    public void onRestaurantsReceived(List<Restaurant> restaurants) {

    }

    /**Initializes the restaurants view**/

    private void initializeRestaurantsView(){
        this.restaurantAdapter=new RestaurantAdapter(this.placesClient, this.restaurants, this.userLocation);
        this.restaurantsView.setAdapter(this.restaurantAdapter);
        this.restaurantsView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
