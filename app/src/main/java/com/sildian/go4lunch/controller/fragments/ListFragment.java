package com.sildian.go4lunch.controller.fragments;


import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.view.ItemClickSupport;
import com.sildian.go4lunch.view.RestaurantAdapter;

import java.util.List;

import butterknife.BindView;

/**************************************************************************************************
 * ListFragment
 * Shows the list of restaurants found in the area and its related information
 *************************************************************************************************/

public class ListFragment extends BaseFragment {

    /**UI components**/

    @BindView(R.id.fragment_list_restaurants) RecyclerView restaurantsView;
    private RestaurantAdapter restaurantAdapter;

    /**Constructor**/

    public ListFragment(PlacesClient placesClient, LatLng userLocation, Workmate currentUser, List<Restaurant> restaurants) {
        super(placesClient, userLocation, currentUser, restaurants);
    }

    /**BaseFragment abstract methods**/

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initializeView(Bundle SavedInstanceState) {
        initializeRestaurantsView();
    }

    @Override
    public void onUserLocationReceived(LatLng userLocation) {

    }

    @Override
    public void onRestaurantsReceived(List<Restaurant> restaurants) {

    }

    /**Initializes the restaurants view**/

    private void initializeRestaurantsView(){

        /*Initializes the different items to create the RecyclerView*/

        this.restaurantAdapter=new RestaurantAdapter(this.placesClient, this.restaurants, this.userLocation);
        this.restaurantsView.setAdapter(this.restaurantAdapter);
        this.restaurantsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*Initializes the item click support*/

        ItemClickSupport.addTo(this.restaurantsView, R.layout.list_restaurant_item)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    startRestaurantActivity(this.restaurants.get(position));
                });
    }
}
