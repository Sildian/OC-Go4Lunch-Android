package com.sildian.go4lunch.controller.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse;
import com.sildian.go4lunch.utils.api.APIStreams;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesLunch;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesRestaurant;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesWorkmate;
import com.sildian.go4lunch.utils.listeners.OnFirebaseQueryResultListener;
import com.sildian.go4lunch.utils.listeners.OnPlaceQueryResultListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**************************************************************************************************
 * BaseActivity
 * Base for all activities
 * Monitors all Firebase and API queries
 *************************************************************************************************/

public abstract class BaseActivity extends AppCompatActivity implements OnFailureListener {

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    protected Workmate currentUser;         //The current user
    private Disposable disposable;          //The disposable which gets the response from the API

    /*********************************************************************************************
     * Abstract methods
     ********************************************************************************************/

    protected abstract void updateUIBeforeQuery();      //Updates the UI before a query runs
    protected abstract void updateUIAfterQuery();       //Updates the UI after a query runs

    /*********************************************************************************************
     * Callbacks
     ********************************************************************************************/

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d("TAG_FIREBASE", e.getMessage());
        showDialog(getString(R.string.dialog_firebase_error_title), getString(R.string.dialog_firebase_error_message));
    }

    /*********************************************************************************************
     * Update methods
     ********************************************************************************************/

    /**Updates the current user**/

    public void updateCurrentUser(Workmate workmate){
        this.currentUser=workmate;
    }

    /*********************************************************************************************
     * Firebase queries
     ********************************************************************************************/

    /**Creates a workmate in Firebase
     * @param user : the user information
     * @param listener : the listener
     */

    public void createWorkmateInFirebase(FirebaseUser user, OnFirebaseQueryResultListener listener){
        String firebaseId=user.getUid();
        String name=user.getDisplayName();
        String imageUrl=user.getPhotoUrl()!=null?user.getPhotoUrl().toString():null;
        Workmate workmate=new Workmate(firebaseId, name, imageUrl);
        FirebaseQueriesWorkmate.createWorkmate(workmate)
                .addOnFailureListener(this)
                .addOnSuccessListener(aVoid ->
                        listener.onGetWorkmateResult(new Workmate(firebaseId, name, imageUrl)));
    }

    /**Creates a restaurant in Firebase
     * @param restaurant : the restaurant
     */

    public void createOrUpdateRestaurantInFirebase(Restaurant restaurant){
        FirebaseQueriesRestaurant.createOrUpdateRestaurant(restaurant)
                .addOnFailureListener(this);
    }

    /**Creates a lunch in Firebase
     * @param restaurant : the restaurant
     * @param workmate : the workmate
     */

    public void createLunchInFirebase(Restaurant restaurant, Workmate workmate){
        FirebaseQueriesLunch.createLunch(restaurant, workmate)
                .addOnFailureListener(this);
    }

    /**Gets a workmate from Firebase
     * @param firebaseId : the firebase id
     * @param listener : the listener
     */

    public void getWorkmateFromFirebase(String firebaseId, OnFirebaseQueryResultListener listener){
        FirebaseQueriesWorkmate.getWorkmate(firebaseId)
                .addOnFailureListener(this)
                .addOnSuccessListener(documentSnapshot ->
                        listener.onGetWorkmateResult(documentSnapshot.toObject(Workmate.class)));
    }

    /**Gets workmates eating at a given restaurant today
     * @param restaurant : the restaurant
     * @param listener : the listener
     */

    public void getWorkmatesEatingAtRestaurantFromFirebase(Restaurant restaurant, OnFirebaseQueryResultListener listener){
        //TODO handle error
        FirebaseQueriesLunch.getWorkmatesEatingAtRestaurant(restaurant)
                .addSnapshotListener((queryDocumentSnapshots, e) ->{
                    if(queryDocumentSnapshots!=null){
                        listener.onGetWorkmatesEatingAtRestaurantResult
                                (restaurant, queryDocumentSnapshots.toObjects(Workmate.class));
                    }
                });
    }

    /**Updates a workmate's likes in Firebase
     * @param workmate : the workmate
     */

    public void updateWorkmateLikesInFirebase(Workmate workmate){
        FirebaseQueriesWorkmate.updateLikes(workmate.getFirebaseId(), workmate.getLikes())
                .addOnFailureListener(this);
    }

    /**Updates a workmate's lunches in Firebase
     * @param workmate : the workmate
     */

    public void updateWorkmateLunchesInFirebase(Workmate workmate){
        FirebaseQueriesWorkmate.updateLunches(workmate.getFirebaseId(), workmate.getLunches())
                .addOnFailureListener(this);
    }

    /**Updates a workmate's settings in Firebase
     * @param workmate : the workmate
     */

    public void updateWorkmateSettingsInFirebase(Workmate workmate){
        FirebaseQueriesWorkmate.updateSettings(workmate.getFirebaseId(), workmate.getSettings())
                .addOnFailureListener(this);
    }

    /**Deletes the user's account and its data in Firebase**/

    public void deleteCurrentUserAccount(){
        FirebaseQueriesWorkmate.deleteWorkmate(this.currentUser.getFirebaseId())
                .addOnFailureListener(this);
        FirebaseAuth.getInstance().getCurrentUser().delete()
                .addOnFailureListener(this);
        FirebaseAuth.getInstance().signOut();
    }

    /**Deletes a lunch in Firebase
     * @param workmate : the workmate
     */

    public void deleteLunchInFirebase(Workmate workmate){
        Restaurant restaurant=workmate.getChosenRestaurantoday();
        if(restaurant!=null) {
            FirebaseQueriesLunch.deleteLunch(restaurant, workmate)
                    .addOnFailureListener(this);
        }
    }

    /*********************************************************************************************
     * API queries
     ********************************************************************************************/

    /**Runs a query to get the list of restaurants near a location and within a radius from Google Places API
     * @param location : the location
     * @param radius : the radius in meters
     * @param listener : the listener
     */

    public void runGooglePlacesSearchQuery(LatLng location, long radius, OnPlaceQueryResultListener listener){

        updateUIBeforeQuery();

        this.disposable= APIStreams.streamGetNearbyRestaurants(this, location, radius)
                .subscribeWith(new DisposableObserver<GooglePlacesSearchResponse>(){

                    @Override
                    public void onNext(GooglePlacesSearchResponse response) {

                        updateUIAfterQuery();

                        /*If the response contains results, then feeds the restaurants list and sends it to the listener*/

                        if(response.getResults()!=null&&!response.getResults().isEmpty()) {
                            List<Restaurant> restaurants=new ArrayList<>();
                            for (GooglePlacesSearchResponse.Result apiRestaurant : response.getResults()) {
                                restaurants.add(new Restaurant(apiRestaurant));
                            }
                            listener.onGetGooglePlacesSearchResult(restaurants);
                        }

                        /*Else shows a dialog to notify the user that no result was found*/

                        else{
                            showDialog(getString(R.string.dialog_api_no_restaurant_found_title),
                                    getString(R.string.dialog_api_no_restaurant_found_message));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG_API", e.getMessage());
                        updateUIAfterQuery();
                        showDialog(getString(R.string.dialog_api_error_title),
                                getString(R.string.dialog_api_error_message));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**Runs a query to get all details about a restaurant from Google and Here APIs
     * @param restaurant : the restaurant
     * @param listener : the listener
     */

    public void runRestaurantAllDetailsQuery(Restaurant restaurant, OnPlaceQueryResultListener listener){

        this.disposable= APIStreams.streamGetRestaurantAllDetails(
                this, restaurant, getResources().getInteger(R.integer.figure_radius_for_details))
                .subscribeWith(new DisposableObserver<Restaurant>(){

                    @Override
                    public void onNext(Restaurant restaurantWithAllDetails) {
                        listener.onGetRestaurantAllDetailsResult(restaurantWithAllDetails);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG_API", e.getMessage());
                        showDialog(getString(R.string.dialog_api_error_title),
                                getString(R.string.dialog_api_error_message));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*********************************************************************************************
     * Dialogs management
     ********************************************************************************************/

    private void showDialog(String title, String message){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNeutralButton("OK", (dialogNeutral, which) -> {

        });
        dialog.create();
        dialog.show();
    }
}
