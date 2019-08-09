package com.sildian.go4lunch.utils.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sildian.go4lunch.model.Message;

/*************************************************************************************************
 * FirebaseQueriesMessage
 * This class provides with queries allowing to store messages within Firebase
 ************************************************************************************************/

public class FirebaseQueriesMessage {

    /**Collection name**/

    private static final String COLLECTION_NAME = "message";

    /**Collection reference**/

    public static CollectionReference getMessageCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**Creates a new message**/

    public static Task<DocumentReference> createMessage(Message message){
        return getMessageCollection().add(message);
    }

    /**Gets the last 50 messages from Firebase**/

    public static Query getLast50Messages(){
        return getMessageCollection().orderBy("date", Query.Direction.DESCENDING).limit(50);
    }
}
