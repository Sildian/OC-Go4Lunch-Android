package com.sildian.go4lunch.controller.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.sildian.go4lunch.R;

/**************************************************************************************************
 * MapFragment
 * Shows the map and allows the user to find and select restaurants
 *************************************************************************************************/

public class MapFragment extends Fragment implements OnMapReadyCallback {

    /**Bundle keys**/

    private static final String KEY_BUNDLE_MAPVIEW = "KEY_BUNDLE_MAPVIEW";

    /**UI Components**/

    private MapView mapView;
    private GoogleMap map;

    /**Constructor**/

    public MapFragment() {
        // Required empty public constructor
    }

    /**Callbacks**/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
    public void onSaveInstanceState(Bundle outState) {
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

        //TODO replace by actions
        Log.i("TAG_MAP", "Map uploaded");
    }
}
