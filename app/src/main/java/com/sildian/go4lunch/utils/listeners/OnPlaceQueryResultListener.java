package com.sildian.go4lunch.utils.listeners;

import com.sildian.go4lunch.model.Restaurant;

import java.util.List;

/**************************************************************************************************
 * OnPlaceQueryResultListener
 * This interface provides with methods allowing to listen and get Places API queries results
 *************************************************************************************************/

public interface OnPlaceQueryResultListener {

    void onGetGooglePlacesSearchResult(List<Restaurant> restaurants);

    void onGetRestaurantAllDetailsResult(Restaurant restaurant);
}
