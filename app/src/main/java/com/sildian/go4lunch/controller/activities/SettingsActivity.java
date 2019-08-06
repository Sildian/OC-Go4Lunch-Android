package com.sildian.go4lunch.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.fragments.RestaurantFragment;
import com.sildian.go4lunch.controller.fragments.SettingsFragment;

/**************************************************************************************************
 * SettingsActivity
 * Allows the user to manage settings
 *************************************************************************************************/

public class SettingsActivity extends AppCompatActivity {

    /**Fragment**/

    private SettingsFragment fragment;

    /**Callbacks**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar(findViewById(R.id.activity_settings_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showFragment();
    }

    /**Shows the fragment**/

    private void showFragment(){
        this.fragment=(SettingsFragment)getSupportFragmentManager().findFragmentById(R.id.activity_settings_fragment);
        this.fragment=new SettingsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_settings_fragment, this.fragment).commit();
    }
}
