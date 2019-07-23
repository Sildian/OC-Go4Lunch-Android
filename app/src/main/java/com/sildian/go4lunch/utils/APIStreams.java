package com.sildian.go4lunch.utils;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.api.GooglePlacesDetailsResponse;
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*************************************************************************************************
 * APIStreams
 * Defines the streams using queries to get data from APIs
 ************************************************************************************************/

public class APIStreams {

    /**Gets restaurants near a location and within a radius
     * @param context : context
     * @param location : the location
     * @param radius : the radius in meters
     * @return a response containing the places
     */

    public static Observable<GooglePlacesSearchResponse> streamGetNearbyRestaurants(Context context, LatLng location, long radius){

        /*Prepares Retrofit queries*/

        GooglePlacesAPIQueries apiQueries=GooglePlacesAPIQueries.retrofit.create(GooglePlacesAPIQueries.class);

        /*Prepares the query's parameters*/

        String queryLocation=location.latitude+","+location.longitude;
        String queryRadius=String.valueOf(radius);
        String queryPlaceType="restaurant";
        String queryApiKey=context.getString(R.string.google_api_key);

        /*Runs the query and returns the response*/

        return apiQueries.getNearbyPlaces(queryLocation, queryRadius, queryPlaceType, queryApiKey)
                .subscribeOn(Schedulers.io())
                .timeout(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**Gets detail information about a restaurant
     * @param context : context
     * @param placeId : the Google place id
     * @return a response containing the information
     */

    public static Observable<GooglePlacesDetailsResponse> streamGetRestaurantDetails(Context context, String placeId){

        /*Prepares Retrofit queries*/

        GooglePlacesAPIQueries apiQueries=GooglePlacesAPIQueries.retrofit.create(GooglePlacesAPIQueries.class);

        /*Prepares the query's parameters*/

        String queryApiKey=context.getString(R.string.google_api_key);

        /*Runs the query and returns the response*/

        return apiQueries.getPlaceDetails(placeId, queryApiKey)
                .subscribeOn(Schedulers.io())
                .timeout(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
