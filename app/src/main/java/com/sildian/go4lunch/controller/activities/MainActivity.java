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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.sildian.go4lunch.controller.fragments.ListFragment;
import com.sildian.go4lunch.controller.fragments.MapFragment;
import com.sildian.go4lunch.controller.fragments.WorkmateFragment;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Settings;
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

    /**Request keys**/

    public static final int KEY_REQUEST_LOGIN=101;
    public static final int KEY_REQUEST_AUTOCOMPLETE=102;
    public static final int KEY_REQUEST_RESTAURANT=103;
    public static final int KEY_REQUEST_SETTINGS=104;
    public static final int KEY_REQUEST_PERMISSION_LOCATION=201;

    /**Bundle keys**/

    public static final String KEY_BUNDLE_USER="KEY_BUNDLE_USER";
    public static final String KEY_BUNDLE_SETTINGS="KEY_BUNDLE_SETTINGS";
    public static final String KEY_BUNDLE_RESTAURANT="KEY_BUNDLE_RESTAURANT";

    /**Location permission**/

    private static final String PERMISSION_LOCATION= Manifest.permission.ACCESS_FINE_LOCATION;

    /**Location and map management**/

    private PlacesClient placesClient;                                      //The placesClient allowing to use Google Places API
    private FusedLocationProviderClient fusedLocationProviderClient;        //Allows to get the location
    private LatLng userLocation;                                            //The user's location
    private List<Restaurant> restaurants;                                   //The list of restaurants in the area

    /**UI Components**/

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationViewHidden;
    private BottomNavigationView navigationBar;
    private BaseFragment fragment;

    /**Callbacks**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeToolbar();
        initializeNavigationDrawer();
        initializeNavigationBar();
        Places.initialize(this, getString(R.string.google_maps_key));
        this.placesClient = Places.createClient(this);
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.restaurants = new ArrayList<>();
        this.settings=new Settings();           //TODO remove this
        initLogin();
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

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getGroupId()){
            case R.id.menu_navigation_group:
                showFragment(menuItem.getItemId());
                break;
            case R.id.menu_navigation_hidden_group:
                handleNavigationDrawerAction(menuItem.getItemId());
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

                if (resultCode == RESULT_OK&&data!=null) {

                    /*Gets the place information*/

                    Place place = Autocomplete.getPlaceFromIntent(data);
                    Log.d("TAG_API", place.getName());
                    Restaurant restaurant=new Restaurant(
                            place.getId(), place.getName(), place.getLatLng().latitude, place.getLatLng().longitude,
                            place.getAddress(), place.getRating());

                    /*Updates the restaurants list and the fragment*/

                    this.restaurants.clear();
                    this.restaurants.add(restaurant);
                    this.fragment.onRestaurantsReceived(this.restaurants);

                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.d("TAG_API", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    //TODO Handle (user canceled)
                }
                break;

            case KEY_REQUEST_RESTAURANT:
                if(data!=null) {
                    this.currentUser = data.getParcelableExtra(KEY_BUNDLE_USER);
                }
                break;

            case KEY_REQUEST_SETTINGS:
                if(data!=null){
                    this.settings=data.getParcelableExtra(KEY_BUNDLE_SETTINGS);
                }
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

    @Override
    public void onGetWorkmateResult(Workmate workmate) {
        this.currentUser=workmate;
        this.fragment.updateCurrentUser(workmate);
        this.settings=new Settings();
        updateNavigationDrawerItems();
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

    /**Initializes the Toolbar**/

    private void initializeToolbar(){
        this.toolbar=findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(this.toolbar);
    }

    /**Initializes the NavigationDrawer**/

    private void initializeNavigationDrawer(){
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        this.navigationViewHidden = findViewById(R.id.activity_main_navigation_view);
        this.navigationViewHidden.setNavigationItemSelectedListener(this);
    }

    /**Initializes the NavigationBar**/

    private void initializeNavigationBar(){
        this.navigationBar = findViewById(R.id.activity_main_navigation_bar);
        this.navigationBar.setOnNavigationItemSelectedListener(this);
    }

    /**Updates the navigation drawer items**/

    private void updateNavigationDrawerItems(){
        ImageView navigationDrawerUserImage=findViewById(R.id.navigation_hidden_header_user_image);
        TextView navigationDrawerUserName=findViewById(R.id.navigation_hidden_header_user_name);
        RequestManager glide= Glide.with(this);
        glide.load(this.currentUser.getImageUrl()).apply(RequestOptions.circleCropTransform()).into(navigationDrawerUserImage);
        navigationDrawerUserName.setText(this.currentUser.getName());
    }

    /**Initializes user login**/

    private void initLogin(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user==null) {
            startLoginActivity();
        }else{
            getWorkmateFromFirebase(user.getUid(), this);
            showFragment(R.id.menu_navigation_map);
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
            createWorkmateInFirebase(FirebaseAuth.getInstance().getCurrentUser(), this);
            showFragment(R.id.menu_navigation_map);
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
     * @param id : the menu id
     */

    private void showFragment(int id){
        this.fragment=(BaseFragment)getSupportFragmentManager().findFragmentById(R.id.activity_main_fragment);
        switch(id){
            case R.id.menu_navigation_map:
                this.fragment = new MapFragment(this.placesClient, this.userLocation, this.currentUser, this.settings, this.restaurants);
                updateUserLocation();
                break;
            case R.id.menu_navigation_list:
                this.fragment = new ListFragment(this.placesClient, this.userLocation, this.currentUser, this.settings, this.restaurants);
                break;
            case R.id.menu_navigation_workmates:
                this.fragment=new WorkmateFragment(this.placesClient, this.userLocation, this.currentUser, this.settings, this.restaurants);
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment, this.fragment)
                .commit();
    }

    /**Handles the NavigationDrawerMenu action according to the id
     * @param id : the menu id
     */

    private void handleNavigationDrawerAction(int id){
        switch(id){
            case R.id.menu_navigation_hidden_lunch:
                if(this.currentUser.getChosenRestaurantoday()!=null) {
                    startRestaurantActivity(this.currentUser.getChosenRestaurantoday());
                }else{
                    //TODO handle
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

    /**Gets the user's location and then searches the nearby restaurants**/

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
                                runGooglePlacesSearchQuery(this.userLocation, this.settings.getSearchRadius(), this);
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
        settingsActivityIntent.putExtra(KEY_BUNDLE_SETTINGS, this.settings);
        startActivityForResult(settingsActivityIntent, KEY_REQUEST_SETTINGS);
    }
}
