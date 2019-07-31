package com.sildian.go4lunch.utils.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sildian.go4lunch.model.firebase.FirebaseLunch;
import com.sildian.go4lunch.model.firebase.FirebaseRestaurant;
import com.sildian.go4lunch.model.firebase.FirebaseWorkmate;

import java.util.Date;

/*************************************************************************************************
 * FirebaseLinkLunch
 * Bridge between the app and Firebase data
 ************************************************************************************************/

public class FirebaseLinkLunch {

    /**Collection name**/

    private static final String COLLECTION_NAME="lunch";

    /**Collection reference**/

    public static CollectionReference getLunchCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**Creates a new lunch
     * @param date : the date of the lunch
     * @param restaurant : the restaurant
     * @param workmate : the workmate
     * @return the resulted task
     */

    public static Task<Void> createLunch(Date date, FirebaseRestaurant restaurant, FirebaseWorkmate workmate){
        String firebaseId=date.toString()+restaurant.getPlaceId()+workmate.getFirebaseId();
        FirebaseLunch lunch=new FirebaseLunch(date, restaurant, workmate);
        return getLunchCollection().document(firebaseId).set(lunch);
    }

    /**Gets all lunchs related to a date and a restaurant
     * @param date : the date
     * @param restaurant : the restaurant
     * @return the result
     */

    public static Query getLunchs(Date date, FirebaseRestaurant restaurant){
        return getLunchCollection().whereEqualTo("date", date).whereEqualTo("restaurant", restaurant);
    }

    /**Gets all lunchs related to a date and a workmate
     * @param date : the date
     * @param workmate : the workmate
     * @return the result
     */

    public static Query getLunchs(Date date, FirebaseWorkmate workmate){
        return getLunchCollection().whereEqualTo("date", date).whereEqualTo("workmate", workmate);
    }

    /**Deletes a lunch related to the given date, restaurant and workmate
     * @param firebaseId : the id
     * @return the resulted task
     */

    public static Task<Void> deleteLunch(String firebaseId) {
        return getLunchCollection().document(firebaseId).delete();
    }
}
