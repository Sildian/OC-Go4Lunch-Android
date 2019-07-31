package com.sildian.go4lunch.utils.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sildian.go4lunch.model.firebase.FirebaseRestaurant;

/*************************************************************************************************
 * FirebaseLinkRestaurant
 * Bridge between the app and Firebase data
 ************************************************************************************************/

public class FirebaseLinkRestaurant {

    /**Collection name**/

    private static final String COLLECTION_NAME="restaurant";

    /**Collection reference**/

    public static CollectionReference getRestaurantCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**Creates a new Restaurant in the database
     * @param placeId : the Google place id
     * @param name : the name
     * @param nbLikes : the number of likes given by workmates
     * @return the resulted task
     */

    public static Task<Void> createRestaurant(String placeId, String name, int nbLikes){
        FirebaseRestaurant restaurant = new FirebaseRestaurant(placeId, name, nbLikes);
        return getRestaurantCollection().document(placeId).set(restaurant);
    }

    /**Gets a restaurant from Firebase
     * @param placeId : the Google place id
     * @return the resulted task
     */

    public static Task<DocumentSnapshot> getRestaurant(String placeId){
        return getRestaurantCollection().document(placeId).get();
    }

    /**Updates the number of likes
     * @param placeId : the Google place id
     * @param nbLikes : the new number of likes
     * @return the resulted task
     */

    public static Task<Void> updateNbLikes(String placeId, String nbLikes) {
        return getRestaurantCollection().document(placeId).update("nbLikes", nbLikes);
    }
}
