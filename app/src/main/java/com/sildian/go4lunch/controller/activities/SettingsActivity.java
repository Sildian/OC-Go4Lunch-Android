package com.sildian.go4lunch.controller.activities;

import android.content.Intent;
import android.os.Bundle;

import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.fragments.SettingsFragment;

/**************************************************************************************************
 * SettingsActivity
 * Allows the user to manage settings
 *************************************************************************************************/

public class SettingsActivity extends BaseActivity {

    /*********************************************************************************************
     * Members
     ********************************************************************************************/

    /**Fragment**/

    private SettingsFragment fragment;          //The fragment

    /*********************************************************************************************
     * Callbacks
     ********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar(findViewById(R.id.activity_settings_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.currentUser=getIntent().getParcelableExtra(MainActivity.KEY_BUNDLE_USER);
        showFragment();
    }

    @Override
    public void onBackPressed() {
        finishSave();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishSave();
        return true;
    }

    /*********************************************************************************************
     * Fragments management
     ********************************************************************************************/

    private void showFragment(){
        this.fragment=(SettingsFragment)getSupportFragmentManager().findFragmentById(R.id.activity_settings_fragment);
        this.fragment=new SettingsFragment(this.currentUser);
        getSupportFragmentManager().beginTransaction().add(R.id.activity_settings_fragment, this.fragment).commit();
    }

    /*********************************************************************************************
     * Activity result management
     ********************************************************************************************/

    /**Finishes the activity after saving the settings**/

    private void finishSave(){
        this.fragment.updateSettings();
        updateWorkmateSettingsInFirebase(this.currentUser);
        setActivityResult(RESULT_OK);
        finish();
    }

    /**Finishes the activity after deleting the user's account**/

    public void finishDeleteAccount(){
        deleteCurrentUserAccount();
        setActivityResult(RESULT_CANCELED);
        finish();
    }

    /**Sets the activity result
     * @param resultCode : the resultCode
     */

    private void setActivityResult(int resultCode){
        Intent resultIntent=new Intent();
        resultIntent.putExtra(MainActivity.KEY_BUNDLE_USER, this.currentUser);
        setResult(resultCode, resultIntent);
    }
}
