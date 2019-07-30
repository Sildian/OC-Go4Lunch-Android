package com.sildian.go4lunch.controller.activities;


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

    /**Data**/

    private PlacesClient placesClient;                  //The placesClient allowing to use Google Places API

    /**UI Components**/

    private RestaurantFragment fragment;

    /**Callbacks**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Places.initialize(this, getString(R.string.google_maps_key));
        this.placesClient = Places.createClient(this);
        this.currentUser=getIntent().getParcelableExtra(MainActivity.KEY_BUNDLE_USER);
        showFragment();
    }

    /**Shows the fragment**/

    private void showFragment(){
        this.fragment=(RestaurantFragment)getSupportFragmentManager().findFragmentById(R.id.activity_restaurant_fragment);
        this.fragment=new RestaurantFragment(this.placesClient);
        getSupportFragmentManager().beginTransaction().add(R.id.activity_restaurant_fragment, this.fragment).commit();
    }
}
