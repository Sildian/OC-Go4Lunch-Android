package com.sildian.go4lunch.controller.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesRestaurant;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesWorkmate;

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
                .addOnSuccessListener(aVoid -> currentUser=new Workmate(firebaseId, name, imageUrl));
    }

    /**Creates a restaurant in Firebase
     * @param restaurant : the restaurant
     */

    public void createOrUpdateRestaurantInFirebase(Restaurant restaurant){
        FirebaseQueriesRestaurant.createOrUpdateRestaurant(restaurant)
                .addOnFailureListener(this);
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
}
