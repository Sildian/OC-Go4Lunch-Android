package com.sildian.go4lunch.controller.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.fragments.MapFragment;

import java.util.Arrays;

/**************************************************************************************************
 * MainActivity
 * Monitors the main user interactions
 *************************************************************************************************/

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    /**Request keys**/

    private static final int KEY_REQUEST_LOGIN=10;

    /**Fragments ids**/

    private static final int ID_FRAGMENT_MAP=0;
    private static final int ID_FRAGMENT_LIST=1;
    private static final int ID_FRAGMENT_WORKMATES=2;

    /**UI Components**/

    private BottomNavigationView navigationBar;
    private Fragment fragment;

    /**Callbacks**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.activity_main_toolbar));
        this.navigationBar=findViewById(R.id.activity_main_navigation_bar);
        this.navigationBar.setOnNavigationItemSelectedListener(this);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user==null) {
            startLoginActivity();
        }

        //TODO move this after login activity
        showFragment(ID_FRAGMENT_MAP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            //TODO replace by an actions
            case R.id.menu_toolbar_search:
                Log.i("TAG_MENU", "Search");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        showFragment(menuItem.getItemId());
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case KEY_REQUEST_LOGIN:
                handleLoginResult(resultCode, data);
                break;
        }
    }

    /**Logs a user in Firebase**/

    private void startLoginActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_go4lunch_main)
                        .build(),
                KEY_REQUEST_LOGIN);
    }

    /**Handles the result when a user logs in Firebase**/

    private void handleLoginResult(int resultCode, Intent data) {

        IdpResponse loginResponse = IdpResponse.fromResultIntent(data);

        //TODO add actions

        if(resultCode == RESULT_OK) {
            Log.d("TAG_LOGIN", "Connected");
        }else {
            if (loginResponse == null) {
                Log.d("TAG_LOGIN", "Canceled");
            }else if(loginResponse.getError()==null){
                Log.d("TAG_LOGIN", "Unknown error");
            }else if (loginResponse.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                Log.d("TAG_LOGIN", "No network");
            }else if (loginResponse.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                Log.d("TAG_LOGIN", "Unknown error");
            }else{
                Log.d("TAG_LOGIN", "Unknown error");
            }
        }
    }

    /**Shows a fragment according to the id
     * @param id : the fragment's id
     */

    private void showFragment(int id){
        this.fragment=getSupportFragmentManager().findFragmentById(R.id.activity_main_fragment);
        switch(id){
            case ID_FRAGMENT_MAP:
                this.fragment = new MapFragment();
                break;
            case ID_FRAGMENT_LIST:
                break;
            case ID_FRAGMENT_WORKMATES:
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment, this.fragment)
                .commit();
    }
}
