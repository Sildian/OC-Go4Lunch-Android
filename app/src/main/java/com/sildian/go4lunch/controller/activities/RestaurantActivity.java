package com.sildian.go4lunch.controller.activities;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.fragments.RestaurantFragment;

/**************************************************************************************************
 * RestaurantActivity
 * Shows a restaurant's information
 *************************************************************************************************/

public class RestaurantActivity extends BaseActivity {

    /*********************************************************************************************
     * Members
     ********************************************************************************************/

    /**Data**/

    private PlacesClient placesClient;      //The placesClient allowing to use Google Places API

    /**Fragment**/

    private RestaurantFragment fragment;    //The fragment

    /*********************************************************************************************
     * Callbacks
     ********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        this.currentUser=getIntent().getParcelableExtra(MainActivity.KEY_BUNDLE_USER);
        initializePlaces();
        initializeResultIntent();
        showFragment();
    }

    /*********************************************************************************************
     * BaseActivity methods
     ********************************************************************************************/

    @Override
    protected void updateUIBeforeQuery() {

    }

    @Override
    protected void updateUIAfterQuery() {

    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private void initializePlaces(){
        Places.initialize(this, getString(R.string.google_maps_key));
        this.placesClient = Places.createClient(this);
    }

    private void initializeResultIntent(){
        Intent resultIntent=new Intent();
        resultIntent.putExtra(MainActivity.KEY_BUNDLE_USER, this.currentUser);
        setResult(RESULT_OK, resultIntent);
    }

    /*********************************************************************************************
     * Fragment management
     ********************************************************************************************/

    private void showFragment(){
        this.fragment=(RestaurantFragment)getSupportFragmentManager().findFragmentById(R.id.activity_restaurant_fragment);
        this.fragment=new RestaurantFragment(this.placesClient);
        getSupportFragmentManager().beginTransaction().add(R.id.activity_restaurant_fragment, this.fragment).commit();
    }
}
