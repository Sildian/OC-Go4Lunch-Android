package com.sildian.go4lunch.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Restaurant;

import java.util.List;

/************************************************************************************************
 * RestaurantAdapter
 * Monitors the restaurants data within a recycler view
 ***********************************************************************************************/

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    /**Information**/

    private PlacesClient placesClient;                      //The placesClient allowing to use Google Places API
    private List<Restaurant> restaurants;                   //The list of articles
    private LatLng userLocation;                            //The user location

    /**Constructor**/

    public RestaurantAdapter(PlacesClient placesClient, List<Restaurant> restaurants, LatLng userLocation){
        this.placesClient=placesClient;
        this.restaurants=restaurants;
        this.userLocation=userLocation;
    }

    /**Adapter methods**/

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_restaurant_item, parent, false);
        return new RestaurantViewHolder(view, this.placesClient);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position, @NonNull List<Object> payloads) {
        holder.update(this.restaurants.get(position), this.userLocation);
    }

    @Override
    public int getItemCount() {
        return this.restaurants.size();
    }
}
