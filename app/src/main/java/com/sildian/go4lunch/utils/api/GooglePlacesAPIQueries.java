package com.sildian.go4lunch.utils.api;

import com.sildian.go4lunch.model.api.GooglePlacesDetailsResponse;
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*************************************************************************************************
 * GooglePlacesAPIQueries
 * Defines the queries getting data from GooglePlaces API
 ************************************************************************************************/

public interface GooglePlacesAPIQueries {

    /**API Urls**/

    String BASE_URL="https://maps.googleapis.com/maps/api/place/";
    String NEARBY_SEARCH_URL="nearbysearch/json?";
    String DETAILS_URL="details/json?";

    /**Retrofit builder**/

    Retrofit retrofit =new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    /**Gets nearby places**/

    @GET(NEARBY_SEARCH_URL)
    Observable<GooglePlacesSearchResponse> getNearbyPlaces(
            @Query("location") String location, @Query("radius") String radius,
            @Query("type") String placeType, @Query("key") String apiKey);

    /**Gets details information about a place**/

    @GET(DETAILS_URL)
    Observable<GooglePlacesDetailsResponse> getPlaceDetails(@Query("placeid") String placeId, @Query("key") String apiKey);
}
