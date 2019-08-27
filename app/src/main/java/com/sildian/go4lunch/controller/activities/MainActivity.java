package com.sildian.go4lunch.controller.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.fragments.BaseFragment;
import com.sildian.go4lunch.controller.fragments.ChatFragment;
import com.sildian.go4lunch.controller.fragments.ListFragment;
import com.sildian.go4lunch.controller.fragments.MapFragment;
import com.sildian.go4lunch.controller.fragments.WorkmateFragment;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.listeners.OnFirebaseQueryResultListener;
import com.sildian.go4lunch.utils.listeners.OnPlaceQueryResultListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**************************************************************************************************
 * MainActivity
 * Monitors the main user interactions
 *************************************************************************************************/

public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        OnFirebaseQueryResultListener, OnPlaceQueryResultListener {

    /*********************************************************************************************
     * Static members
     ********************************************************************************************/

    /**Request keys**/

    public static final int KEY_REQUEST_LOGIN=101;
    public static final int KEY_REQUEST_AUTOCOMPLETE=102;
    public static final int KEY_REQUEST_RESTAURANT=103;
    public static final int KEY_REQUEST_SETTINGS=104;
    public static final int KEY_REQUEST_PERMISSION_LOCATION=201;

    /**Bundle keys**/

    public static final String KEY_BUNDLE_USER="KEY_BUNDLE_USER";
    public static final String KEY_BUNDLE_RESTAURANT="KEY_BUNDLE_RESTAURANT";

    /**Location permission**/

    private static final String PERMISSION_LOCATION= Manifest.permission.ACCESS_FINE_LOCATION;

    /*********************************************************************************************
     * Members
     ********************************************************************************************/

    /**Location and map management**/

    private PlacesClient placesClient;                                      //The placesClient allowing to use Google Places API
    private FusedLocationProviderClient fusedLocationProviderClient;        //Allows to get the location
    private LatLng userLocation;                                            //The user's location
    private List<Restaurant> restaurants;                                   //The list of restaurants in the area

    /**UI Components**/

    private RequestManager glide;                                           //Request manager allowing to show images
    private Toolbar toolbar;                                                //The top toolbar
    private ProgressBar progressBar;                                        //The progress bar
    private DrawerLayout drawerLayout;                                      //The drawer allowing to show the hidden navigation view
    private NavigationView navigationViewHidden;                            //The hidden navigation view on the left
    private ImageView navigationDrawerUserImage;                            //The user image shown into the hidden navigation view
    private TextView navigationDrawerUserNameText;                          //The user name shown into the hidden navigation view
    private BottomNavigationView navigationBar;                             //The bottom navigation bar
    private BaseFragment fragment;                                          //The fragment

    /*********************************************************************************************
     * Callbacks
     ********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.restaurants = new ArrayList<>();
        glide=Glide.with(this);
        initializeToolbar();
        initializeProgressBar();
        initializeNavigationDrawer();
        initializeNavigationBar();
        initializePlaces();
        initializeLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_toolbar_search:
                startAutocompleteActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getGroupId()){
            case R.id.menu_navigation_hidden_group:
                handleNavigationDrawerAction(menuItem.getItemId());
                break;
            case R.id.menu_navigation_group:
                showFragment(menuItem.getItemId());
            break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case KEY_REQUEST_LOGIN:
                handleLoginResult(resultCode, data);
                break;
            case KEY_REQUEST_AUTOCOMPLETE:
                handleAutocompleteResult(resultCode, data);
                break;
            case KEY_REQUEST_RESTAURANT:
                handleRestaurantResult(resultCode, data);
                break;
            case KEY_REQUEST_SETTINGS:
                handleSettingsResult(resultCode, data);
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
                            showDialog(getString(R.string.dialog_location_permission_title),
                                    getString(R.string.dialog_location_permission_message));
                            break;
                    }
                }
                break;
        }
    }

    @Override
    public void onGetWorkmateResult(Workmate workmate) {

        /*If the workmate doesn't exist in Firebase, then creates it. Else, gets its related data and shows MapFragment.*/

        if(workmate==null){
            createWorkmateInFirebase(FirebaseAuth.getInstance().getCurrentUser(), this);
        }else {
            this.currentUser = workmate;
            updateNavigationDrawerItems();
            showFragment(R.id.menu_navigation_map);
        }
    }

    @Override
    public void onGetRestaurantResult(Restaurant restaurant) {

    }

    @Override
    public void onGetWorkmatesEatingAtRestaurantResult(Restaurant restaurant, List<Workmate> workmates) {

    }

    @Override
    public void onGetGooglePlacesSearchResult(List<Restaurant> restaurants) {
        this.restaurants.clear();
        this.restaurants.addAll(restaurants);
        this.fragment.onRestaurantsReceived(restaurants);
    }

    @Override
    public void onGetRestaurantAllDetailsResult(Restaurant restaurant) {

    }

    /*********************************************************************************************
     * BaseActivity methods
     ********************************************************************************************/

    @Override
    protected void updateUIBeforeQuery() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void updateUIAfterQuery() {
        this.progressBar.setVisibility(View.GONE);
    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private void initializeToolbar(){
        this.toolbar=findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(this.toolbar);
    }

    private void initializeProgressBar(){
        this.progressBar=findViewById(R.id.activity_main_progress_bar);
    }

    private void initializeNavigationDrawer(){

        /*Navigation drawer*/

        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        /*Navigation view*/

        this.navigationViewHidden = findViewById(R.id.activity_main_navigation_view);
        this.navigationViewHidden.setNavigationItemSelectedListener(this);

        /*Header views*/

        View navigationViewHiddenHeader=getLayoutInflater()
                .inflate(R.layout.navigation_hidden_header, this.navigationViewHidden);
        this.navigationDrawerUserImage=navigationViewHiddenHeader
                .findViewById(R.id.navigation_hidden_header_user_image);
        this.navigationDrawerUserNameText=navigationViewHiddenHeader
                .findViewById(R.id.navigation_hidden_header_user_name);
    }

    private void initializeNavigationBar(){
        this.navigationBar = findViewById(R.id.activity_main_navigation_bar);
        this.navigationBar.setOnNavigationItemSelectedListener(this);
    }

    private void initializePlaces(){
        Places.initialize(this, getString(R.string.google_maps_key));
        this.placesClient = Places.createClient(this);
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void initializeLogin(){

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        /*If no user is connected, then starts the loginActivity. Else gets the user's data from Firebase*/

        if(user==null) {
            startLoginActivity();
        }else{
            getWorkmateFromFirebase(user.getUid(), this);
        }
    }

    /*********************************************************************************************
     * Update methods
     ********************************************************************************************/

    private void updateNavigationDrawerItems(){
        this.glide.load(this.currentUser.getImageUrl())
                .apply(RequestOptions.circleCropTransform()).into(this.navigationDrawerUserImage);
        this.navigationDrawerUserNameText.setText(this.currentUser.getName());
    }

    /*********************************************************************************************
     * Starts Activities
     ********************************************************************************************/

    /**Starts an activity to log a user in Firebase**/

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

    /**Starts an activity to search restaurants by filling a text**/

    private void startAutocompleteActivity(){

        /*Prepares bounds to restrict the research*/

        RectangularBounds locationRestriction=RectangularBounds.newInstance(
                new LatLng(this.userLocation.latitude-0.01, this.userLocation.longitude-0.01),
                new LatLng(this.userLocation.latitude+0.01, this.userLocation.longitude+0.01));

        /*Prepares the fields to be retrieved*/

        List<Place.Field> fields = Arrays.asList(
                Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.RATING);

        /*Runs the autocomplete intent*/

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setHint(getString(R.string.hint_search_restaurant))
                .setLocationRestriction(locationRestriction)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(this);
        startActivityForResult(intent, KEY_REQUEST_AUTOCOMPLETE);
    }

    /**Starts the RestaurantActivity
     * @param restaurant : the restaurant to display
     */

    protected void startRestaurantActivity(Restaurant restaurant){
        Intent restaurantActivityIntent = new Intent(this, RestaurantActivity.class);
        restaurantActivityIntent.putExtra(MainActivity.KEY_BUNDLE_USER, this.currentUser);
        restaurantActivityIntent.putExtra(MainActivity.KEY_BUNDLE_RESTAURANT, restaurant);
        startActivityForResult(restaurantActivityIntent, KEY_REQUEST_RESTAURANT);
    }

    /**Starts the SettingsActivity**/

    protected void startSettingsActivity(){
        Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);
        settingsActivityIntent.putExtra(KEY_BUNDLE_USER, this.currentUser);
        startActivityForResult(settingsActivityIntent, KEY_REQUEST_SETTINGS);
    }

    /*********************************************************************************************
     * Handles activities results
     ********************************************************************************************/

    /**Handles the result when a user logs in Firebase**/

    private void handleLoginResult(int resultCode, Intent data) {

        IdpResponse loginResponse = IdpResponse.fromResultIntent(data);

        /*If the response is a success, then shows a message and gets the user's data from Firebase*/

        if(resultCode == RESULT_OK) {

            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
            String text=getString(R.string.toast_login_success)+" "+user.getDisplayName()+".";
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            getWorkmateFromFirebase(user.getUid(), this);

            /*Else shows a message and leaves the app*/

        }else {

            if (loginResponse == null) {
                Toast.makeText(this, R.string.toast_login_canceled, Toast.LENGTH_LONG).show();
            }else if(loginResponse.getError()==null){
                Toast.makeText(this, R.string.toast_login_unknown_error, Toast.LENGTH_LONG).show();
            }else if (loginResponse.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                Toast.makeText(this, R.string.toast_login_no_network, Toast.LENGTH_LONG).show();
            }else if (loginResponse.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                Toast.makeText(this, R.string.toast_login_unknown_error, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, R.string.toast_login_unknown_error, Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    /**Handles the result when a user search for a restaurant by filling a text**/

    private void handleAutocompleteResult(int resultCode, Intent data){

        if (resultCode == RESULT_OK) {

            if(data!=null) {

                /*Gets the place information*/

                Place place = Autocomplete.getPlaceFromIntent(data);
                Restaurant restaurant = new Restaurant(
                        place.getId(), place.getName(), place.getLatLng().latitude, place.getLatLng().longitude,
                        place.getAddress(), place.getRating());

                /*Updates the restaurants list and the fragment*/

                this.restaurants.clear();
                this.restaurants.add(restaurant);
                this.fragment.onRestaurantsReceived(this.restaurants);

            }else{
                showDialog(getString(R.string.dialog_api_no_restaurant_found_title),
                        getString(R.string.dialog_api_no_restaurant_found_message));
            }

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.d("TAG_API", status.getStatusMessage());
            showDialog(getString(R.string.dialog_api_error_title),
                    getString(R.string.dialog_api_error_message));

        } else if (resultCode == RESULT_CANCELED) {

        }
    }

    /**Handles the result from RestaurantActivity**/

    private void handleRestaurantResult(int resultCode, Intent data){
        if(resultCode==RESULT_OK) {
            if (data != null) {
                this.currentUser = data.getParcelableExtra(KEY_BUNDLE_USER);
            }
        }
    }

    /**Handles the result from SettingsActivity**/

    private void handleSettingsResult(int resultCode, Intent data){

        /*If result ok, then updates the current user data*/

        if(resultCode==RESULT_OK) {
            if (data != null) {
                this.currentUser = data.getParcelableExtra(KEY_BUNDLE_USER);
            }

            /*Else, assumes that the user just deleted its account, then restarts login*/

        }else if(resultCode==RESULT_CANCELED){
            initializeLogin();
        }
    }

    /*********************************************************************************************
     * Fragments management
     ********************************************************************************************/

    /**Shows a fragment according to the id
     * @param id : the menu id
     */

    private void showFragment(int id){

        this.fragment=(BaseFragment)getSupportFragmentManager().findFragmentById(R.id.activity_main_fragment);
        switch(id){

            case R.id.menu_navigation_map:
                this.fragment = new MapFragment(this.placesClient, this.userLocation, this.currentUser, this.restaurants);
                if (this.currentUser != null) {
                    updateUserLocation();
                }
                break;

            case R.id.menu_navigation_list:
                this.fragment = new ListFragment(this.placesClient, this.userLocation, this.currentUser, this.restaurants);
                break;

            case R.id.menu_navigation_workmates:
                this.fragment=new WorkmateFragment(this.placesClient, this.userLocation, this.currentUser, this.restaurants);
                break;

            case R.id.menu_navigation_chat:
                this.fragment=new ChatFragment(this.placesClient, this.userLocation, this.currentUser, this.restaurants);
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment, this.fragment)
                .commitAllowingStateLoss();
    }

    /*********************************************************************************************
     * Navigation management
     ********************************************************************************************/

    /**Handles the NavigationDrawerMenu action according to the id
     * @param id : the menu id
     */

    private void handleNavigationDrawerAction(int id){

        switch(id){

            case R.id.menu_navigation_hidden_lunch:
                if(this.currentUser.getChosenRestaurantoday()!=null) {
                    startRestaurantActivity(this.currentUser.getChosenRestaurantoday());
                }else{
                    Toast.makeText(this, R.string.toast_navigation_my_lunch_null, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.menu_navigation_hidden_settings:
                startSettingsActivity();
                break;

            case R.id.menu_navigation_hidden_logout:
                FirebaseAuth.getInstance().signOut();
                startLoginActivity();
                break;
        }
    }

    /*********************************************************************************************
     * Location management
     ********************************************************************************************/

    public void updateUserLocation() {

        /*Checks if the location research can begin*/

        if (this.fusedLocationProviderClient!=null) {
            if (Build.VERSION.SDK_INT<23||checkSelfPermission(PERMISSION_LOCATION)== PackageManager.PERMISSION_GRANTED) {

                /*Begins the location research*/

                this.fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                this.userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                this.fragment.onUserLocationReceived(this.userLocation);
                                runGooglePlacesSearchQuery(this.userLocation, this.currentUser.getSettings().getSearchRadius(), this);
                            } else {
                                showDialog(getString(R.string.dialog_location_error_title),
                                        getString(R.string.dialog_location_error_message));
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.d("TAG_LOCATION", e.getMessage());
                            showDialog(getString(R.string.dialog_location_error_title),
                                    getString(R.string.dialog_location_error_message));
                        });

            }else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_LOCATION)) {
                showDialog(getString(R.string.dialog_location_permission_title),
                        getString(R.string.dialog_location_permission_message));
            }else{
                ActivityCompat.requestPermissions(this, new String[]{PERMISSION_LOCATION}, KEY_REQUEST_PERMISSION_LOCATION);
            }
        }else{
            showDialog(getString(R.string.dialog_location_error_title),
                    getString(R.string.dialog_location_error_message));
        }
    }

    public void searchRestaurantsAtCameraLocation(LatLng cameraLocation){
        runGooglePlacesSearchQuery(cameraLocation, this.currentUser.getSettings().getSearchRadius(), this);
    }
}
