package com.sildian.go4lunch.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.controller.activities.MainActivity;
import com.sildian.go4lunch.controller.activities.RestaurantActivity;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;

import java.util.List;

import butterknife.ButterKnife;

/**************************************************************************************************
 * BaseFragment
 * Base for all fragments attached to MainActivity
 *************************************************************************************************/

public abstract class BaseFragment extends Fragment {

    /*********************************************************************************************
     * Members
     ********************************************************************************************/

    protected PlacesClient placesClient;                                        //The placesClient allowing to use Google Places API
    protected LatLng userLocation;                                              //The user's location
    protected Workmate currentUser;                                             //The current user
    protected List<Restaurant> restaurants;                                     //The list of restaurants in the area

    /*********************************************************************************************
     * Abstract methods
     ********************************************************************************************/

    protected abstract int getFragmentLayout();                                 //Gets the fragment layout
    protected abstract void initializeView(Bundle SavedInstanceState);          //Initializes the view
    public abstract void onUserLocationReceived(LatLng userLocation);           //Handles the user's location result
    public abstract void onRestaurantsReceived(List<Restaurant> restaurants);   //Handles the restaurants result

    /*********************************************************************************************
     * Constructors
     ********************************************************************************************/

    public BaseFragment(){

    }

    public BaseFragment(PlacesClient placesClient, LatLng userLocation, Workmate currentUser, List<Restaurant> restaurants) {
        this.placesClient=placesClient;
        this.userLocation=userLocation;
        this.currentUser=currentUser;
        this.restaurants=restaurants;
    }

    /*********************************************************************************************
     * Callbacks
     ********************************************************************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getFragmentLayout(), container, false);
        ButterKnife.bind(this, view);
        initializeView(savedInstanceState);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case MainActivity.KEY_REQUEST_RESTAURANT:
                if(data!=null){
                    updateCurrentUser(data.getParcelableExtra(MainActivity.KEY_BUNDLE_USER));
                }
        }
    }

    /*********************************************************************************************
     * Updates
     ********************************************************************************************/

    /**Updates the current user
     * @param workmate : the user's data
     */

    protected void updateCurrentUser(Workmate workmate){
        this.currentUser=workmate;
        MainActivity activity=(MainActivity) getActivity();
        activity.updateCurrentUser(this.currentUser);
    }

    /*********************************************************************************************
     * Starts activities
     ********************************************************************************************/

    /**Starts the RestaurantActivity
     * @param restaurant : the restaurant to display
     */

    protected void startRestaurantActivity(Restaurant restaurant){
        Intent restaurantActivityIntent = new Intent(getActivity(), RestaurantActivity.class);
        restaurantActivityIntent.putExtra(MainActivity.KEY_BUNDLE_USER, this.currentUser);
        restaurantActivityIntent.putExtra(MainActivity.KEY_BUNDLE_RESTAURANT, restaurant);
        startActivityForResult(restaurantActivityIntent, MainActivity.KEY_REQUEST_RESTAURANT);
    }
}
