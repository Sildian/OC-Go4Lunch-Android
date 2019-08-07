package com.sildian.go4lunch.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.activities.MainActivity;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.DateUtilities;
import com.sildian.go4lunch.utils.api.APIStreams;
import com.sildian.go4lunch.utils.listeners.OnFirebaseQueryResultListener;
import com.sildian.go4lunch.utils.listeners.OnPlaceQueryResultListener;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/************************************************************************************************
 * RestaurantViewHolder
 * Displays the items related to a restaurant in a RecyclerView
 ***********************************************************************************************/

public class RestaurantViewHolder extends RecyclerView.ViewHolder
        implements OnFirebaseQueryResultListener, OnPlaceQueryResultListener {

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

    /**Constructor**/

    public RestaurantViewHolder(View itemView, PlacesClient placesClient){
        super(itemView);
        this.itemView=itemView;
        ButterKnife.bind(this, itemView);
        this.placesClient=placesClient;
    }

    /**Callbacks**/

    @Override
    public void onGetWorkmateResult(Workmate workmate) {

    }

    @Override
    public void onGetWorkmatesEatingAtRestaurantResult(Restaurant restaurant, List<Workmate> workmates) {
        if(!workmates.isEmpty()){
            this.nbWorkmatesText.setVisibility(View.VISIBLE);
            this.nbWorkmatesText.setText(String.valueOf(workmates.size()));
        }
    }

    @Override
    public void onGetGooglePlacesSearchResult(List<Restaurant> restaurants) {

    }

    @Override
    public void onGetRestaurantAllDetailsResult(Restaurant restaurant) {
        cuisineTypeText.setText(restaurant.getCuisineType());
        updateOpeningHours(restaurant);
    }

    /**Updates with a restaurant
     * @param restaurant : the restaurant
     * @param userLocation : the user location
     */

    public void update(Restaurant restaurant, LatLng userLocation){
        MainActivity activity=(MainActivity) this.itemView.getContext();
        this.nameText.setText(restaurant.getName());
        this.cuisineTypeText.setText("");
        this.addressText.setText(restaurant.getAddress());
        this.openingHoursText.setText("");
        String distance=restaurant.getDistanceInMeters(userLocation)+" m";
        this.distanceText.setText(distance);
        this.nbWorkmatesText.setVisibility(View.INVISIBLE);
        activity.getWorkmatesEatingAtRestaurantFromFirebase(restaurant, this);
        this.starsRatingBar.setRating(restaurant.getNbStars());
        APIStreams.streamGetRestaurantImage(this.placesClient, restaurant, this.imageView);
        activity.runRestaurantAllDetailsQuery(restaurant, this);
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
