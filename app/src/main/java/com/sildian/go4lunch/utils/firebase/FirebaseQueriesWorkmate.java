package com.sildian.go4lunch.utils.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;

import java.util.List;

/*************************************************************************************************
 * FirebaseQueriesWorkmate
 * This class provides with queries allowing to store workmates within Firebase
 ************************************************************************************************/

public class FirebaseQueriesWorkmate {

    /**Collection name**/

    private static final String COLLECTION_NAME = "workmate";

    /**Collection reference**/

    public static CollectionReference getWorkmateCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**Creates a new workmate**/

    public static Task<Void> createWorkmate(Workmate workmate) {
        return getWorkmateCollection().document(workmate.getFirebaseId()).set(workmate);
    }

    /**Gets a workmate**/

    public static Task<DocumentSnapshot> getWorkmate(String firebaseId){
        return getWorkmateCollection().document(firebaseId).get();
    }

    /**Gets all workmates**/

    public static Query getAllWorkmates(){
        return getWorkmateCollection()
                .orderBy("name")
                .limit(50);
    }

    /**Updates likes**/

    public static Task<Void> updateLikes(String firebaseId, List<Restaurant> likes) {
        return getWorkmateCollection().document(firebaseId).update("likes", likes);
    }

    /**Updates lunches**/

    public static Task<Void> updateLunches(String firebaseId, List<Workmate.Lunch> lunches) {
        return getWorkmateCollection().document(firebaseId).update("lunches", lunches);
    }

    /**Deletes a workmate**/

    public static Task<Void> deleteWorkmate(String firebaseId) {
        return getWorkmateCollection().document(firebaseId).delete();
    }
}
