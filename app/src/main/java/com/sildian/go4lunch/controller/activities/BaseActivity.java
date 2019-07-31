package com.sildian.go4lunch.controller.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.firebase.FirebaseLinkWorkmate;

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

    /***Creates a workmate in Firebase
     * @param user : the user information
     */

    protected void createWorkmateInFirebase(FirebaseUser user){
        final String firebaseId=user.getUid();
        final String name=user.getDisplayName();
        final String imageUrl=user.getPhotoUrl()!=null?user.getPhotoUrl().toString():null;
        FirebaseLinkWorkmate.createWorkmate(firebaseId, name, imageUrl)
                .addOnFailureListener(this)
                .addOnSuccessListener(aVoid -> currentUser=new Workmate(firebaseId, name, imageUrl));
    }
}
