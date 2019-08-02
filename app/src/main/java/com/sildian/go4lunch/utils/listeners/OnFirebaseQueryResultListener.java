package com.sildian.go4lunch.utils.listeners;

import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;

import java.util.List;

/**************************************************************************************************
 * OnFirebaseQueryResultListener
 * This interface provides with methods allowing to listen and get Firebase queries results
 *************************************************************************************************/

public interface OnFirebaseQueryResultListener {

    void onGetWorkmateResult(Workmate workmate);

    void onGetWorkmatesEatingAtRestaurantResult(Restaurant restaurant, List<Workmate> workmates);
}
