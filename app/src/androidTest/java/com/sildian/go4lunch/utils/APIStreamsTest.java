package com.sildian.go4lunch.utils;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.gms.maps.model.LatLng;
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse;

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
}