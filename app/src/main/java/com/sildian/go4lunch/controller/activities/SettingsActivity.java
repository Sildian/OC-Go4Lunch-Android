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

    /**Fragment**/

    private SettingsFragment fragment;

    /**Callbacks**/

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

    /**Shows the fragment**/

    private void showFragment(){
        this.fragment=(SettingsFragment)getSupportFragmentManager().findFragmentById(R.id.activity_settings_fragment);
        this.fragment=new SettingsFragment(this.currentUser);
        getSupportFragmentManager().beginTransaction().add(R.id.activity_settings_fragment, this.fragment).commit();
    }

    /**Finishes the activity after saving the settings**/

    private void finishSave(){
        this.fragment.updateSettings();
        updateWorkmateSettingsInFirebase(this.currentUser);
        setActivityResult(false);
        finish();
    }

    /**Finishes the activity after logging out**/

    private void finishLogout(){
        setActivityResult(true);
        finish();
    }

    /**Sets the activity result
     * @param logout : true if the user is logging out
     */

    private void setActivityResult(boolean logout){
        Intent resultIntent=new Intent();
        resultIntent.putExtra(MainActivity.KEY_BUNDLE_USER, this.currentUser);
        if(logout){
            setResult(RESULT_CANCELED, resultIntent);
        }else {
            setResult(RESULT_OK, resultIntent);
        }
    }
}
