package com.sildian.go4lunch.controller.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesLunch;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesRestaurant;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesWorkmate;
import com.sildian.go4lunch.utils.listeners.OnFirebaseQueryResultListener;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;

/**************************************************************************************************
 * BaseActivity
 * Base for all activities
 *************************************************************************************************/

public abstract class BaseActivity extends AppCompatActivity implements OnFailureListener {

    /**Data**/

    protected Workmate currentUser;                         //The current user

    /**Callbacks**/

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d("TAG_FIREBASE", e.getMessage());
        //TODO Handle Firebase errors
    }

    /**Creates a workmate in Firebase
     * @param user : the user information
     */

    public void createWorkmateInFirebase(FirebaseUser user){
        String firebaseId=user.getUid();
        String name=user.getDisplayName();
        String imageUrl=user.getPhotoUrl()!=null?user.getPhotoUrl().toString():null;
        Workmate workmate=new Workmate(firebaseId, name, imageUrl);
        FirebaseQueriesWorkmate.createWorkmate(workmate)
                .addOnFailureListener(this)
                .addOnSuccessListener(aVoid -> this.currentUser=new Workmate(firebaseId, name, imageUrl));
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
     */

    public void getWorkmateFromFirebase(String firebaseId){
        FirebaseQueriesWorkmate.getWorkmate(firebaseId)
                .addOnFailureListener(this)
                .addOnSuccessListener(documentSnapshot -> this.currentUser = documentSnapshot.toObject(Workmate.class));
    }

    /**Gets workmates eating at a given restaurant today
     * @param restaurant : the restaurant
     * @param listener : the listener
     */

    public void getWorkmatesEatingAtRestaurantFromFirebase(Restaurant restaurant, OnFirebaseQueryResultListener listener){
        FirebaseQueriesLunch.getWorkmatesEatingAtRestaurant(restaurant)
                .addSnapshotListener((queryDocumentSnapshots, e) ->
                        listener.onGetWorkmatesEatingAtRestaurantResult
                                (restaurant, queryDocumentSnapshots.toObjects(Workmate.class)));
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

    /**Deletes a lunch in Firebase
     * @param workmate : the workmate
     */

    public void deleteLunchInFirebase(Workmate workmate){

        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date=calendar.getTime();

        int id=-1;
        for(Workmate.Lunch lunch:workmate.getLunches()){
            if(lunch.getDate().equals(date)){
                id=workmate.getLunches().indexOf(lunch);
            }
        }

        if(id!=-1) {
            FirebaseQueriesLunch.deleteLunch(workmate.getLunches().get(id).getRestaurant(), workmate)
                    .addOnFailureListener(this);
        }
    }
}
