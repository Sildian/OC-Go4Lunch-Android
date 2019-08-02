package com.sildian.go4lunch.utils.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;

import java.util.Calendar;
import java.util.Date;

/*************************************************************************************************
 * FirebaseQueriesLunch
 * This class provides with queries allowing to store lunches within Firebase
 ************************************************************************************************/

public class FirebaseQueriesLunch {

    /**Collection names**/

    private static final String COLLECTION_NAME = "lunch";
    private static final String COLLECTION_NAME_DATE="date";
    private static final String COLLECTION_NAME_WORKMATE="workmate";

    /**Collection reference**/

    public static CollectionReference getLunchCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**Creates a new lunch**/

    public static Task<Void> createLunch(Restaurant restaurant, Workmate workmate){
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date=calendar.getTime();
        return getLunchCollection().document(restaurant.getPlaceId())
                .collection(COLLECTION_NAME_DATE).document(date.toString())
                .collection(COLLECTION_NAME_WORKMATE).document(workmate.getFirebaseId())
                .set(workmate);
    }

    /**Gets workmates eating at a given restaurant today**/

    public static Query getWorkmatesEatingAtRestaurant(Restaurant restaurant){
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date=calendar.getTime();
        return getLunchCollection().document(restaurant.getPlaceId())
                .collection(COLLECTION_NAME_DATE).document(date.toString())
                .collection(COLLECTION_NAME_WORKMATE)
                .orderBy("name");
    }

    /**Deletes a lunch**/

    public static Task<Void> deleteLunch(Restaurant restaurant, Workmate workmate) {
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date=calendar.getTime();
        return getLunchCollection().document(restaurant.getPlaceId())
                .collection(COLLECTION_NAME_DATE).document(date.toString())
                .collection(COLLECTION_NAME_WORKMATE).document(workmate.getFirebaseId())
                .delete();
    }
}
