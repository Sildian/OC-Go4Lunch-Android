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
import com.sildian.go4lunch.utils.DateUtilities;
import com.sildian.go4lunch.utils.api.APIStreams;

import java.util.Calendar;

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
                        updateOpeningHours(restaurantWithAllDetails);
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

    /**Updates the opening hours
     * @param restaurantWithAllDetails : the restaurant
     */

    private void updateOpeningHours(Restaurant restaurantWithAllDetails){

        /*Prepares the parameters*/

        Calendar calendar= Calendar.getInstance();
        String openingHours;
        Restaurant.Period currentOpeningHours=restaurantWithAllDetails.getCurrentOpeningHours(calendar);

        /*If the current opening hours are null, then shows hours unknown*/

        if(currentOpeningHours==null){
            openingHours=itemView.getContext().getString(R.string.text_restaurant_opening_hours_unknown);

            /*If the current opening hours return day -1, then shows restaurant closed*/

        }else if(currentOpeningHours.getDay()==-1){
            openingHours=itemView.getContext().getString(R.string.text_restaurant_opening_hours_closed);

            /*Else formats the opening hours and shows it*/

        }else{
            String inputTimeFormat="HHmm";
            String ouputTimeFormat= DateUtilities.Companion.getLocalTimeFormatPattern();
            String openTime=DateUtilities.Companion.convertFormat
                    (inputTimeFormat, ouputTimeFormat, currentOpeningHours.getOpenTime());
            String closeTime=DateUtilities.Companion.convertFormat
                    (inputTimeFormat, ouputTimeFormat, currentOpeningHours.getCloseTime());
            openingHours=itemView.getContext().getString
                    (R.string.text_restaurant_opening_hours_open) +" "+openTime+" - "+closeTime;
        }
        openingHoursText.setText(openingHours);
    }
}
