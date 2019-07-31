package com.sildian.go4lunch.utils.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sildian.go4lunch.model.firebase.FirebaseLike;
import com.sildian.go4lunch.model.firebase.FirebaseRestaurant;
import com.sildian.go4lunch.model.firebase.FirebaseWorkmate;

/*************************************************************************************************
 * FirebaseLinkLike
 * Bridge between the app and Firebase data
 ************************************************************************************************/

public class FirebaseLinkLike {

    /**Collection name**/

    private static final String COLLECTION_NAME="like";

    /**Collection reference**/

    public static CollectionReference getLikeCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**Creates a new like
     * @param restaurant : the restaurant
     * @param workmate : the workmate
     * @return the resulted task
     */

    public static Task<DocumentReference> createLike(FirebaseRestaurant restaurant, FirebaseWorkmate workmate){
        FirebaseLike like=new FirebaseLike(restaurant, workmate);
        return getLikeCollection().add(like);
    }

    /**Gets all likes related to a restaurant
     * @param restaurant : the restaurant
     * @return the result
     */

    public static Query getLikes(FirebaseRestaurant restaurant){
        return getLikeCollection().whereEqualTo("restaurant", restaurant);
    }

    /**Gets all likes related to a workmate
     * @param workmate : the workmate
     * @return the result
     */

    public static Query getLikes(FirebaseWorkmate workmate){
        return getLikeCollection().whereEqualTo("workmate", workmate);
    }
}
