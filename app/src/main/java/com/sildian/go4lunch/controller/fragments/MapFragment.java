package com.sildian.go4lunch.controller.fragments;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.activities.MainActivity;
import com.sildian.go4lunch.model.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**************************************************************************************************
 * MapFragment
 * Shows the map and allows the user to find and select restaurants
 *************************************************************************************************/

public class MapFragment extends BaseFragment implements OnMapReadyCallback {

    /**Bundle keys**/

    private static final String KEY_BUNDLE_MAPVIEW = "KEY_BUNDLE_MAPVIEW";

    /**UI Components**/

    private FloatingActionButton locationButton;
    private MapView mapView;
    private GoogleMap map;

    /**Constructor**/

    public MapFragment(PlacesClient placesClient, LatLng userLocation, List<Restaurant> restaurants) {
        super(placesClient, userLocation, restaurants);
    }

    /**Callbacks**/

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        initializeLocationButton(view);
        initializeMapView(view, savedInstanceState);
        return view;
    }

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
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mapView.onStop();
    }

    @Override
    public void onPause() {
        this.mapView.onPause();
        super.onPause();
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
                        .icon(getBitmapDescriptor(R.drawable.ic_restaurant_location_off)));
            }
        }
    }

    /**Initializes the location button**/

    private void initializeLocationButton(View view){
        this.locationButton=view.findViewById(R.id.fragment_map_button_location);
        this.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity=(MainActivity) getActivity();
                mainActivity.updateUserLocation();
            }
        });
    }

    /**Initializes the map view**/

    private void initializeMapView(View view, Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(KEY_BUNDLE_MAPVIEW);
        }
        this.mapView = view.findViewById(R.id.fragment_map_map_view);
        this.mapView.onCreate(mapViewBundle);
        this.mapView.getMapAsync(this);
    }

    //TODO move this to an other class

    private BitmapDescriptor getBitmapDescriptor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable vectorDrawable = (VectorDrawable) getActivity().getDrawable(id);
            int h = vectorDrawable.getIntrinsicHeight();
            int w = vectorDrawable.getIntrinsicWidth();
            vectorDrawable.setBounds(0, 0, w, h);
            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bm);
        } else {
            return BitmapDescriptorFactory.fromResource(id);
        }
    }
}
