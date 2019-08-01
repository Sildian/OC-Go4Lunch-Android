package com.sildian.go4lunch.utils.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sildian.go4lunch.model.Restaurant;

/*************************************************************************************************
 * FirebaseQueriesRestaurant
 * This class provides with queries allowing to store restaurants within Firebase
 ************************************************************************************************/

public class FirebaseQueriesRestaurant {

    /**Collection name**/

    private static final String COLLECTION_NAME = "restaurant";

    /**Collection reference**/

    public static CollectionReference getRestaurantCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**Creates or updates a restaurant in Firebase**/

    public static Task<Void> createOrUpdateRestaurant(Restaurant restaurant) {
        return getRestaurantCollection().document(restaurant.getPlaceId()).set(restaurant);
    }

    /**Gets a restaurant**/

    public static Task<DocumentSnapshot> getRestaurant(String placeId){
        return getRestaurantCollection().document(placeId).get();
    }
}
