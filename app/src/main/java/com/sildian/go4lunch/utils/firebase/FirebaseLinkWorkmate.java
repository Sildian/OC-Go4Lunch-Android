package com.sildian.go4lunch.utils.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sildian.go4lunch.model.firebase.FirebaseWorkmate;

/*************************************************************************************************
 * FirebaseLinkWorkmate
 * Bridge between the app and Firebase data
 ************************************************************************************************/

public class FirebaseLinkWorkmate {

    /**Collection name**/

    private static final String COLLECTION_NAME="workmate";

    /**Collection reference**/

    public static CollectionReference getWorkmateCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**Creates a new workmate to create in the database
     * @param firebaseId : the firebase id
     * @param name : the name
     * @param imageUrl : the image url
     * @return the resulted task
     */

    public static Task<Void> createWorkmate(String firebaseId, String name, String imageUrl) {
        FirebaseWorkmate workmate = new FirebaseWorkmate(name, imageUrl);
        return getWorkmateCollection().document(firebaseId).set(workmate);
    }

    /**Gets a workmate from Firebase
     * @param firebaseId : the firebase id
     * @return : the resulted task
     */

    public static Task<DocumentSnapshot> getWorkmate(String firebaseId){
        return getWorkmateCollection().document(firebaseId).get();
    }

    /**Gets all workmates from Firebase**/

    public static Query getAllWorkmates(){
        return getWorkmateCollection().orderBy("name").limit(50);
    }

    /**Deletes a workmate in Firebase
     * @param firebaseId : the firebase id
     * @return the resulted task
     */

    public static Task<Void> deleteWorkmate(String firebaseId) {
        return getWorkmateCollection().document(firebaseId).delete();
    }
}
