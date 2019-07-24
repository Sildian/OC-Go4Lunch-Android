package com.sildian.go4lunch.controller.fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.activities.MainActivity;

import org.jetbrains.annotations.NotNull;

/**************************************************************************************************
 * MapFragment
 * Shows the map and allows the user to find and select restaurants
 *************************************************************************************************/

public class MapFragment extends BaseFragment implements OnMapReadyCallback {

    /**Bundle keys**/

    private static final String KEY_BUNDLE_MAPVIEW = "KEY_BUNDLE_MAPVIEW";

    /**UI Components**/

    private MapView mapView;
    private GoogleMap map;

    /**Constructor**/

    public MapFragment(LatLng userLocation) {
        super(userLocation);
    }

    /**Callbacks**/

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(KEY_BUNDLE_MAPVIEW);
        }
        this.mapView = view.findViewById(R.id.fragment_map_map_view);
        this.mapView.onCreate(mapViewBundle);
        this.mapView.getMapAsync(this);
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
        this.map.addMarker(new MarkerOptions().position(this.userLocation));
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(this.userLocation, 15));
    }
}
