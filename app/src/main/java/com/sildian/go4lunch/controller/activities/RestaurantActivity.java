package com.sildian.go4lunch.controller.activities;


import android.os.Bundle;

import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.fragments.RestaurantFragment;

/**************************************************************************************************
 * RestaurantActivity
 * Shows a restaurant's information
 *************************************************************************************************/

public class RestaurantActivity extends BaseActivity {

    /**UI Components**/

    private RestaurantFragment fragment;

    /**Callbacks**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        this.currentUser=getIntent().getParcelableExtra(MainActivity.KEY_BUNDLE_USER);
        showFragment();
    }

    /**Shows the fragment**/

    private void showFragment(){
        this.fragment=(RestaurantFragment)getSupportFragmentManager().findFragmentById(R.id.activity_restaurant_fragment);
        this.fragment=new RestaurantFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_restaurant_fragment, this.fragment).commit();
    }
}
