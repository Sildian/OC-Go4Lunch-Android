package com.sildian.go4lunch.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.gms.maps.model.LatLng;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Restaurant;

import java.util.List;

/************************************************************************************************
 * RestaurantAdapter
 * Monitors the restaurants data within a recycler view
 ***********************************************************************************************/

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    /**Information**/

    private List<Restaurant> restaurants;                   //The list of articles
    private RequestManager glide;                           //The request manager to display images
    private LatLng userLocation;                            //The user location

    /**Constructor**/

    public RestaurantAdapter(List<Restaurant> restaurants, RequestManager glide, LatLng userLocation){
        this.restaurants=restaurants;
        this.glide=glide;
        this.userLocation=userLocation;
    }

    /**Adapter methods**/

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_restaurant_item, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position, @NonNull List<Object> payloads) {
        holder.update(this.restaurants.get(position), this.glide, this.userLocation);
    }

    @Override
    public int getItemCount() {
        return this.restaurants.size();
    }
}
