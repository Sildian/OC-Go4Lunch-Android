package com.sildian.go4lunch.view;

import android.util.Log;
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
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/************************************************************************************************
 * RestaurantViewHolder
 * Displays the items related to a restaurant in a RecyclerView
 ***********************************************************************************************/

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    /**UI components**/

    private View itemView;
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
    private Disposable disposable;                      //The disposable which gets the response from the API

    /**Constructor**/

    public RestaurantViewHolder(View itemView, PlacesClient placesClient){
        super(itemView);
        this.itemView=itemView;
        ButterKnife.bind(this, itemView);
        this.placesClient=placesClient;
    }

    /**Updates with a restaurant
     * @param restaurant : the restaurant
     * @param userLocation : the user location
     */

    public void update(Restaurant restaurant, LatLng userLocation){
        this.nameText.setText(restaurant.getName());
        this.cuisineTypeText.setText("");
        this.addressText.setText(restaurant.getAddress());
        this.openingHoursText.setText("");
        String distance=restaurant.getDistanceInMeters(userLocation)+" m";
        this.distanceText.setText(distance);
        this.workmatesIcon.setVisibility(restaurant.getLunchWorkmates().size()>0?View.VISIBLE:View.INVISIBLE);
        this.nbWorkmatesText.setVisibility(restaurant.getLunchWorkmates().size()>0?View.VISIBLE:View.INVISIBLE);
        this.nbWorkmatesText.setText(String.valueOf(restaurant.getLunchWorkmates().size()));
        this.starsRatingBar.setRating(restaurant.getNbStars());
        APIStreams.streamGetRestaurantImage(this.placesClient, restaurant, this.imageView);
        runRestaurantAllDetailsQuery(restaurant);
    }

    /**Runs a query to get all details about a restaurant from Google and Here APIs, and update the related fields
     * @param restaurant : the restaurant
     */

    private void runRestaurantAllDetailsQuery(Restaurant restaurant){
        //TODO change radius to value
        this.disposable= APIStreams.streamGetRestaurantAllDetails(this.itemView.getContext(), restaurant, 100)
                .subscribeWith(new DisposableObserver<Restaurant>(){
                    @Override
                    public void onNext(Restaurant restaurantWithAllDetails) {
                        cuisineTypeText.setText(restaurantWithAllDetails.getCuisineType());
                        if(restaurantWithAllDetails.getOpeningHours().size()>0) {
                            openingHoursText.setText(restaurantWithAllDetails.getOpeningHours().get(0).getCloseTime());
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
