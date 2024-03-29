package com.sildian.go4lunch.utils.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.api.GooglePlacesDetailsResponse;
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse;
import com.sildian.go4lunch.model.api.HerePlacesResponse;
import com.sildian.go4lunch.view.AutocompleteSuggestionAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
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
        String queryPlaceType=context.getString(R.string.query_place_type);
        String queryApiKey=context.getString(R.string.google_api_key);

        /*Runs the query and returns the response*/

        return apiQueries.getNearbyPlaces(queryLocation, queryRadius, queryPlaceType, queryApiKey)
                .subscribeOn(Schedulers.io())
                .timeout(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**Gets restaurants near a location and within a radius, specifying the name
     * @param context : context
     * @param location : the location
     * @param radius : the radius in meters
     * @param restaurantName : the name of the restaurant
     * @return a response containing the places
     */

    public static Observable<GooglePlacesSearchResponse> streamGetNearbyRestaurants(
            Context context, LatLng location, long radius, String restaurantName){

        /*Prepares Retrofit queries*/

        GooglePlacesAPIQueries apiQueries=GooglePlacesAPIQueries.retrofit.create(GooglePlacesAPIQueries.class);

        /*Prepares the query's parameters*/

        String queryLocation=location.latitude+","+location.longitude;
        String queryRadius=String.valueOf(radius);
        String queryPlaceType=context.getString(R.string.query_place_type);
        String queryApiKey=context.getString(R.string.google_api_key);

        /*Runs the query and returns the response*/

        return apiQueries.getNearbyPlaces(queryLocation, queryRadius, queryPlaceType, restaurantName, queryApiKey)
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

    /**Gets extra details information about a restaurant from Here API
     * @param context : the context
     * @param location : the location
     * @param radius : the radius in meters
     * @param restaurantName : the restaurant's name
     * @return : a response containing the information
     */

    public static Observable<HerePlacesResponse> streamGetRestaurantExtraDetails
            (Context context, LatLng location, long radius, String restaurantName){

        /*Prepares Retrofit queries*/

        HerePlacesAPIQueries apiQueries=HerePlacesAPIQueries.retrofit.create(HerePlacesAPIQueries.class);

        /*Prepares the query's parameters*/

        String queryArea=location.latitude+","+location.longitude+";r="+radius;
        String queryPlaceType=context.getString(R.string.query_place_type);
        String queryAppId=context.getString(R.string.here_app_id);
        String queryAppCode=context.getString(R.string.here_app_code);

        /*Runs the query and returns the response*/

        return apiQueries.getPlaceExtraDetails(context.getString(R.string.query_language),
                queryArea, queryPlaceType, restaurantName, queryAppId, queryAppCode)
                .subscribeOn(Schedulers.io())
                .timeout(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**Gets all detail information about a restaurant : both details and extra details
     * @param context : the context
     * @param restaurant : the restaurant
     * @param radius : the radius in meters
     * @return the restaurant containing all the additional detail information
     */

    public static Observable<Restaurant> streamGetRestaurantAllDetails
            (Context context, Restaurant restaurant, long radius){

        /*Runs the query to get details from GooglePlacesDetails API*/

        return streamGetRestaurantDetails(context, restaurant.getPlaceId())
                .map(response -> {
                    restaurant.addDetails(response.getResult());
                    return restaurant;
                })

                /*Then runs the query to get extra details from HerePlaces API*/

                .flatMap((Function<Restaurant, Observable<Restaurant>>) restaurantWithDetails ->
                        streamGetRestaurantExtraDetails(context,
                                new LatLng(restaurantWithDetails.getLocationLat(), restaurantWithDetails.getLocationLng()),
                                radius, restaurantWithDetails.getName())
                        .map(response -> {
                            restaurantWithDetails.addExtraDetails(response.getResults().getItems().get(0));
                            return restaurantWithDetails;
                        })
                .onErrorReturnItem(restaurant));
    }

    /**Gets a restaurant's main image
     * @param placesClient : the placesClient allowing to use Google Places API
     * @param restaurant : the restaurant
     * @param imageView : the imageView displaying the image
     */

    public static void streamGetRestaurantImage(PlacesClient placesClient, Restaurant restaurant, ImageView imageView) {

        /*Prepares the query's parameters*/

        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);
        FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(restaurant.getPlaceId(), fields);

        /*Runs the query to get the reference*/

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {

            Place place = response.getPlace();

            if(place.getPhotoMetadatas()!=null) {
                PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
                String attributions = photoMetadata.getAttributions();

                /*Prepares the query to get the image*/

                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500)
                        .setMaxHeight(300)
                        .build();

                /*Runs the query to get the image*/

                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    imageView.setImageBitmap(bitmap);

                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        int statusCode = apiException.getStatusCode();
                        Log.d("TAG_API", exception.getMessage());
                    }
                });
            }
        });
    }

    /**Uses Google Places Autocomplete API to get restaurants near a location
     * @param placesClient : the placesClient allowing to use Google Places API
     * @param location : the location
     * @param keyword : the query keyword
     * @param predictions : the list of suggestions returned by the API
     * @param adapter : the adapter displaying the results
     */

    public static void streamGetAutocompleteRestaurants(PlacesClient placesClient, LatLng location,
                                                   String keyword, List<AutocompletePrediction> predictions,
                                                   AutocompleteSuggestionAdapter adapter){

        /*Creates a new token for the autocomplete session*/

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        /*Prepares bounds to restrict the research*/

        RectangularBounds locationRestriction=RectangularBounds.newInstance(
                new LatLng(location.latitude-0.01, location.longitude-0.01),
                new LatLng(location.latitude+0.01, location.longitude+0.01));

        /*Creates a FindAutocompletePredictionsRequest*/

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationRestriction(locationRestriction)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setSessionToken(token)
                .setQuery(keyword)
                .build();

        /*Runs the request and, if the successful, feeds the recyclerView*/

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener((response) -> {
                    predictions.clear();
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        if (prediction.getPlaceTypes().contains(Place.Type.RESTAURANT)) {
                            predictions.add(prediction);
                        }
                    }
                    adapter.notifyDataSetChanged();

                })
                .addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.d("TAG_API", apiException.getMessage());
                    }
                });
    }
}
