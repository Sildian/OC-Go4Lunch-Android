package com.sildian.go4lunch.utils;

import com.sildian.go4lunch.model.api.HerePlacesResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*************************************************************************************************
 * HerePlacesAPIQueries
 * Defines the queries getting data from HerePlaces API
 * https://developer.here.com
 ************************************************************************************************/

public interface HerePlacesAPIQueries {

    /**API Urls**/

    String BASE_URL="https://places.cit.api.here.com/places/v1/";
    String BROWSE_URL="browse?";

    /**Retrofit builder**/

    Retrofit retrofit =new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    /**Gets extra detail information about a place**/

    @GET(BROWSE_URL)
    Observable<HerePlacesResponse> getPlaceExtraDetails(
            @Query("in") String area, @Query("cat") String placeType, @Query("name") String placeName,
            @Query("app_id") String appId, @Query("app_code") String appCode);
}
