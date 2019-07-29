package com.sildian.go4lunch.utils;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.gms.maps.model.LatLng;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.api.GooglePlacesDetailsResponse;
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse;
import com.sildian.go4lunch.model.api.HerePlacesResponse;
import com.sildian.go4lunch.utils.api.APIStreams;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.*;

public class APIStreamsTest{

    @Test
    public void given_MyPositionAndRadius1500_when_streamGetNearbyRestaurants_then_checkResponseHasResults(){

        Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        LatLng location=new LatLng(48.942682, 2.137834);
        long radius=1500;

        Observable<GooglePlacesSearchResponse> observable= APIStreams.streamGetNearbyRestaurants(context, location, radius);
        TestObserver<GooglePlacesSearchResponse> testObserver=new TestObserver<>();
        observable.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        if(testObserver.errorCount()>0){
            for(Throwable error:testObserver.errors()) {
                Log.d("TAG_API", error.getMessage());
            }
        }

        GooglePlacesSearchResponse response=testObserver.values().get(0);
        assertNotNull(response.getResults());
        assertTrue(response.getResults().size()>0);
    }

    @Test
    public void given_ToyamaId_when_streamGetRestaurantDetails_then_checkResponseIsToyama(){

        Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        String placeId="ChIJh0aPKn9h5kcRaOvBGMJDv50";

        Observable<GooglePlacesDetailsResponse> observable= APIStreams.streamGetRestaurantDetails(context, placeId);
        TestObserver<GooglePlacesDetailsResponse> testObserver=new TestObserver<>();
        observable.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        if(testObserver.errorCount()>0){
            for(Throwable error:testObserver.errors()) {
                Log.d("TAG_API", error.getMessage());
            }
        }

        GooglePlacesDetailsResponse response=testObserver.values().get(0);
        assertEquals("Toyama", response.getResult().getName());
    }

    @Test
    public void given_ToyamaNearMyPosition_when_streamGetRestaurantExtraDetails_then_checkTagIsAsian(){

        Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        LatLng location=new LatLng(48.942682, 2.137834);
        long radius=1500;
        String restaurantName="Toyama";

        Observable<HerePlacesResponse> observable= APIStreams.streamGetRestaurantExtraDetails(context, location, radius, restaurantName);
        TestObserver<HerePlacesResponse> testObserver=new TestObserver<>();
        observable.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        if(testObserver.errorCount()>0){
            for(Throwable error:testObserver.errors()) {
                Log.d("TAG_API", error.getMessage());
            }
        }

        HerePlacesResponse response=testObserver.values().get(0);
        assertNotNull(response.getResults());
        assertNotNull(response.getResults().getItems());
        assertEquals("Asian", response.getResults().getItems().get(0).getTags().get(0).getTitle());
    }

    @Test
    public void given_Toyama_when_streamGetRestaurantAllDetails_then_check_NameIsToyamaAndTagIsJapanese(){

        Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        String placeId="ChIJh0aPKn9h5kcRaOvBGMJDv50";
        LatLng location=new LatLng(48.94556, 2.151771);
        long radius=100;

        Restaurant restaurant=new Restaurant(placeId, "Toyama", location, "Fake address", 5.0);

        Observable<Restaurant> observable= APIStreams.streamGetRestaurantAllDetails(context, restaurant, radius);
        TestObserver<Restaurant> testObserver=new TestObserver<>();
        observable.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        if(testObserver.errorCount()>0){
            for(Throwable error:testObserver.errors()) {
                Log.d("TAG_API", error.getMessage());
            }
        }

        Restaurant restaurantWithDetails=testObserver.values().get(0);
        assertEquals("Toyama", restaurantWithDetails.getName());
        assertEquals("Japanese", restaurantWithDetails.getCuisineType());
    }
}