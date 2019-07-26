package com.sildian.go4lunch.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.utils.api.APIStreams;

import butterknife.BindView;
import butterknife.ButterKnife;

/************************************************************************************************
 * RestaurantViewHolder
 * Displays the items related to a restaurant in a RecyclerView
 ***********************************************************************************************/

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    /**UI components**/

    @BindView(R.id.list_restaurant_item_name) TextView nameText;
    @BindView(R.id.list_restaurant_item_cuisine_type) TextView cuisineTypeText;
    @BindView(R.id.list_restaurant_item_address) TextView addressText;
    @BindView(R.id.list_restaurant_item_opening_hours) TextView openingHoursText;
    @BindView(R.id.list_restaurant_item_distance) TextView distanceText;
    @BindView(R.id.list_restaurant_item_workmates_icon) ImageView workmatesIcon;
    @BindView(R.id.list_restaurant_item_nb_workmates) TextView nbWorkmatesText;
    @BindView(R.id.list_restaurant_item_stars) RatingBar starsRatingBar;
    @BindView(R.id.list_restaurant_item_image) ImageView imageView;

    /**Information**/

    private PlacesClient placesClient;                  //The placesClient allowing to use Google Places API

    /**Constructor**/

    public RestaurantViewHolder(View itemView, PlacesClient placesClient){
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.placesClient=placesClient;
    }

    /**Updates with a restaurant
     * @param restaurant : the restaurant
     * @param userLocation : the user location
     */

    public void update(Restaurant restaurant, LatLng userLocation){
        this.nameText.setText(restaurant.getName());
        this.cuisineTypeText.setText(restaurant.getCuisineType());
        this.addressText.setText(restaurant.getAddress());
        //this.openingHoursText.setText(restaurant.getOpeningHours());
        String distance=restaurant.getDistanceInMeters(userLocation)+" m";
        this.distanceText.setText(distance);
        this.workmatesIcon.setVisibility(restaurant.getLunchWorkmates().size()>0?View.VISIBLE:View.INVISIBLE);
        this.nbWorkmatesText.setVisibility(restaurant.getLunchWorkmates().size()>0?View.VISIBLE:View.INVISIBLE);
        this.nbWorkmatesText.setText(String.valueOf(restaurant.getLunchWorkmates().size()));
        this.starsRatingBar.setRating(restaurant.getNbStars());
        APIStreams.streamGetRestaurantImage(this.placesClient, restaurant, this.imageView);
    }
}
