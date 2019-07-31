package com.sildian.go4lunch.controller.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.model.firebase.FirebaseRestaurant;
import com.sildian.go4lunch.model.firebase.FirebaseWorkmate;
import com.sildian.go4lunch.utils.firebase.FirebaseLinkLike;
import com.sildian.go4lunch.utils.firebase.FirebaseLinkLunch;
import com.sildian.go4lunch.utils.firebase.FirebaseLinkRestaurant;
import com.sildian.go4lunch.utils.firebase.FirebaseLinkWorkmate;

import java.util.Calendar;
import java.util.Date;

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
        final String firebaseId=user.getUid();
        final String name=user.getDisplayName();
        final String imageUrl=user.getPhotoUrl()!=null?user.getPhotoUrl().toString():null;
        FirebaseLinkWorkmate.createWorkmate(firebaseId, name, imageUrl)
                .addOnFailureListener(this)
                .addOnSuccessListener(aVoid -> currentUser=new Workmate(firebaseId, name, imageUrl));
    }

    /**Creates a restaurant in Firebase
     * @param restaurant : the restaurant
     */

    public void createRestaurantInFirebase(Restaurant restaurant){
        final String placeId=restaurant.getPlaceId();
        final String name=restaurant.getName();
        final int nbLikes=restaurant.getNbLikes();
        FirebaseLinkRestaurant.createRestaurant(placeId, name, nbLikes)
                .addOnFailureListener(this);
    }

    /**Creates a like in Firebase
     * @param restaurant : the restaurant
     * @param workmate : the workmate
     */

    public void createLikeInFirebase(Restaurant restaurant, Workmate workmate){
        final FirebaseRestaurant firebaseRestaurant=new FirebaseRestaurant
                (restaurant.getPlaceId(), restaurant.getName(), restaurant.getNbLikes());
        final FirebaseWorkmate firebaseWorkmate=new FirebaseWorkmate
                (workmate.getFirebaseId(), workmate.getName(), workmate.getImageUrl());
        FirebaseLinkLike.createLike(firebaseRestaurant, firebaseWorkmate)
                .addOnFailureListener(this);
    }

    /**Creates a lunch in Firebase
     * @param restaurant : the restaurant
     * @param workmate : the workmate
     */

    public void createLunchInFirebase(Restaurant restaurant, Workmate workmate){
        final Calendar calendar= Calendar.getInstance();
        final Date date=calendar.getTime();
        final FirebaseRestaurant firebaseRestaurant=new FirebaseRestaurant
                (restaurant.getPlaceId(), restaurant.getName(), restaurant.getNbLikes());
        final FirebaseWorkmate firebaseWorkmate=new FirebaseWorkmate
                (workmate.getFirebaseId(), workmate.getName(), workmate.getImageUrl());
        FirebaseLinkLunch.createLunch(date, firebaseRestaurant, firebaseWorkmate)
                .addOnFailureListener(this);
    }
}
