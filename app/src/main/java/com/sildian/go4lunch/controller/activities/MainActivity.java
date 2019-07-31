package com.sildian.go4lunch.controller.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.fragments.BaseFragment;
import com.sildian.go4lunch.controller.fragments.ListFragment;
import com.sildian.go4lunch.controller.fragments.MapFragment;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse;
import com.sildian.go4lunch.utils.api.APIStreams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**************************************************************************************************
 * MainActivity
 * Monitors the main user interactions
 *************************************************************************************************/

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    /**Request keys**/

    private static final int KEY_REQUEST_LOGIN=101;
    private static final int KEY_REQUEST_PERMISSION_LOCATION=201;

    /**Bundle keys**/

    public static final String KEY_BUNDLE_USER="KEY_BUNDLE_USER";
    public static final String KEY_BUNDLE_RESTAURANT="KEY_BUNDLE_RESTAURANT";

    /**Location permission**/

    private static final String PERMISSION_LOCATION= Manifest.permission.ACCESS_FINE_LOCATION;

    /**Location and map management**/

    private PlacesClient placesClient;                                      //The placesClient allowing to use Google Places API
    private FusedLocationProviderClient fusedLocationProviderClient;        //Allows to get the location
    private LatLng userLocation;                                            //The user's location
    private List<Restaurant> restaurants;                                   //The list of restaurants in the area
    private Disposable disposable;                                          //The disposable which gets the response from the API

    /**UI Components**/

    private BottomNavigationView navigationBar;
    private BaseFragment fragment;

    /**Callbacks**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.activity_main_toolbar));
        Places.initialize(this, getString(R.string.google_maps_key));
        this.placesClient = Places.createClient(this);
        this.navigationBar=findViewById(R.id.activity_main_navigation_bar);
        this.navigationBar.setOnNavigationItemSelectedListener(this);
        this.fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        this.restaurants=new ArrayList<>();

        //TODO Improve this

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user==null) {
            startLoginActivity();
        }else{
            this.currentUser=new Workmate(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString());
            initializeMapAndLocation();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case KEY_REQUEST_PERMISSION_LOCATION:
                if(grantResults.length>0){
                    switch(grantResults[0]){
                        case PackageManager.PERMISSION_GRANTED:
                            updateUserLocation();
                            break;
                        case PackageManager.PERMISSION_DENIED:
                            //TODO Handle
                            break;
                    }
                }
                break;
        }
    }

    /**Initializes the map and the user's location**/

    private void initializeMapAndLocation(){
        showFragment(R.id.menu_navigation_map);
        updateUserLocation();
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
            createWorkmateInFirebase(FirebaseAuth.getInstance().getCurrentUser());
            initializeMapAndLocation();
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
        this.fragment=(BaseFragment)getSupportFragmentManager().findFragmentById(R.id.activity_main_fragment);
        switch(id){
            case R.id.menu_navigation_map:
                this.fragment = new MapFragment(this.placesClient, this.userLocation, this.currentUser, this.restaurants);
                break;
            case R.id.menu_navigation_list:
                this.fragment = new ListFragment(this.placesClient, this.userLocation, this.currentUser, this.restaurants);
                break;
            case R.id.menu_navigation_workmates:
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment, this.fragment)
                .commit();
    }

    /**Gets the user's location and then searches the nearby restaurants**/

    public void updateUserLocation() {

        /*Checks if the location research can begin*/

        if (this.fusedLocationProviderClient!=null) {
            if (Build.VERSION.SDK_INT<23||checkSelfPermission(PERMISSION_LOCATION)== PackageManager.PERMISSION_GRANTED) {

                /*Begins the location research*/

                this.fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                fragment.onUserLocationReceived(userLocation);

                                //TODO replace radius by a variable
                                runGooglePlacesSearchQuery(userLocation, 1500);
                            } else {
                                //TODO handle
                            }
                        })
                        .addOnFailureListener(e -> {
                            //TODO handle
                        });

            }else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_LOCATION)) {
                //TODO handle
            }else{
                ActivityCompat.requestPermissions(this, new String[]{PERMISSION_LOCATION}, KEY_REQUEST_PERMISSION_LOCATION);
            }
        }else{
            //TODO handle
        }
    }

    /**Runs a query to get the list of restaurants near a location and within a radius
     * @param location : the location
     * @param radius : the radius in meters
     */

    private void runGooglePlacesSearchQuery(LatLng location, long radius){
        this.disposable= APIStreams.streamGetNearbyRestaurants(this, location, radius)
                .subscribeWith(new DisposableObserver<GooglePlacesSearchResponse>(){
            @Override
            public void onNext(GooglePlacesSearchResponse response) {
                if(response.getResults()!=null) {
                    restaurants.clear();
                    for (GooglePlacesSearchResponse.Result apiRestaurant : response.getResults()) {
                        restaurants.add(new Restaurant(apiRestaurant));
                    }
                    fragment.onRestaurantsReceived(restaurants);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("TAG_API", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
