package com.sildian.go4lunch.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.activities.MainActivity;
import com.sildian.go4lunch.controller.activities.RestaurantActivity;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.ImageUtilities;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;

/**************************************************************************************************
 * MapFragment
 * Shows the map and allows the user to find and select restaurants
 *************************************************************************************************/

public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    /**Bundle keys**/

    private static final String KEY_BUNDLE_MAPVIEW = "KEY_BUNDLE_MAPVIEW";

    /**UI Components**/

    @BindView(R.id.fragment_map_map_view) MapView mapView;
    @BindView(R.id.fragment_map_button_location) FloatingActionButton locationButton;
    @BindView(R.id.fragment_map_restaurant_description) LinearLayout restaurantDescriptionLayout;
    @BindView(R.id.fragment_map_text_restaurant_name) TextView restaurantNameText;
    @BindView(R.id.fragment_map_restaurant_stars) RatingBar restaurantStars;
    @BindView(R.id.fragment_map_text_restaurant_distance) TextView restaurantDistanceText;
    @BindView(R.id.fragment_map_text_restaurant_address) TextView restaurantAddressText;
    @BindView(R.id.fragment_map_button_restaurant) Button restaurantButton;
    private BottomSheetBehavior bottomSheet;
    private GoogleMap map;

    /**Constructor**/

    public MapFragment(PlacesClient placesClient, LatLng userLocation, Workmate currentUser, List<Restaurant> restaurants) {
        super(placesClient, userLocation, currentUser, restaurants);
    }

    /**Callbacks**/

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(KEY_BUNDLE_MAPVIEW);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(KEY_BUNDLE_MAPVIEW, mapViewBundle);
        }
        this.mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    @Override
    public void onPause() {
        this.mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mapView.onStop();
    }

    @Override
    public void onDestroy() {
        this.mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map=googleMap;
        this.map.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.bottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        if(marker.getTag()!=null){
            Restaurant restaurant=(Restaurant)marker.getTag();
            updateBottomSheet(restaurant);
            return true;
        }else {
            return false;
        }
    }

    /**BaseFragment abstract methods**/

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map;
    }

    @Override
    protected void initializeView(Bundle savedInstanceState) {
        initializeLocationButton();
        initializeMapView(savedInstanceState);
        initializeRestaurantButton();
        this.bottomSheet=BottomSheetBehavior.from(this.restaurantDescriptionLayout);
    }

    @Override
    public void onUserLocationReceived(LatLng userLocation) {
        this.userLocation=userLocation;
        if(this.map!=null) {
            this.map.addMarker(new MarkerOptions().position(this.userLocation));
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(this.userLocation, 15));
        }
    }

    @Override
    public void onRestaurantsReceived(List<Restaurant> restaurants) {
        this.restaurants=restaurants;
        if(this.map!=null) {
            for (Restaurant restaurant : this.restaurants) {
                this.map.addMarker(new MarkerOptions()
                        .position(restaurant.getLocation())
                        .icon(ImageUtilities.getBitmapDescriptor(getActivity(), R.drawable.ic_restaurant_location_off)))
                .setTag(restaurant);
            }
        }
    }

    /**Initializes the location button**/

    private void initializeLocationButton(){
        this.locationButton.setOnClickListener(v -> {
            MainActivity mainActivity=(MainActivity) getActivity();
            mainActivity.updateUserLocation();
        });
    }

    /**Initializes the map view**/

    private void initializeMapView(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(KEY_BUNDLE_MAPVIEW);
        }
        this.mapView.onCreate(mapViewBundle);
        this.mapView.getMapAsync(this);
    }

    /**Initializes the restaurant button**/

    private void initializeRestaurantButton(){
        this.restaurantButton.setOnClickListener(v -> {
            if(v.getTag()!=null) {
                Intent restaurantActivityIntent = new Intent(getActivity(), RestaurantActivity.class);
                restaurantActivityIntent.putExtra(MainActivity.KEY_BUNDLE_USER, currentUser);
                restaurantActivityIntent.putExtra(MainActivity.KEY_BUNDLE_RESTAURANT, (Restaurant) v.getTag());
                startActivity(restaurantActivityIntent);
            }
        });
    }

    /**Updates the bottomSheet with a restaurant's information
     * @param restaurant : the restaurant
     */

    private void updateBottomSheet(Restaurant restaurant){
        this.restaurantNameText.setText(restaurant.getName());
        this.restaurantStars.setRating(restaurant.getNbStars());
        String distance=restaurant.getDistanceInMeters(userLocation)+" m";
        this.restaurantDistanceText.setText(distance);
        this.restaurantAddressText.setText(restaurant.getAddress());
        this.restaurantButton.setTag(restaurant);
        this.bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
