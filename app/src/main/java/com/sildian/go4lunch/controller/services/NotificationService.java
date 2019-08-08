package com.sildian.go4lunch.controller.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.activities.MainActivity;
import com.sildian.go4lunch.controller.activities.RestaurantActivity;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesWorkmate;

/*************************************************************************************************
 * NotificationService
 * Handles the notifications received from Firebase
 ************************************************************************************************/

public class NotificationService extends FirebaseMessagingService {

    /**Notification information**/

    public static final String CHANEL_NOTIFICATION="CHANEL_NOTIFICATION";
    public static final String CHANEL_NAME="Go4Lunch notification";
    public static final int NOTIFICATION_ID=10;

    /**Data**/

    private Workmate currentUser;                       //The current user

    /**Callback**/

    @Override
    public void onCreate() {
        super.onCreate();
        getWorkmateFromFirebase();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            getWorkmateFromFirebase();
        }
    }

    /**Gets the current user's data from Firebase**/

    private void getWorkmateFromFirebase(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseQueriesWorkmate.getWorkmate(user.getUid())
                .addOnFailureListener(e -> {
                    Log.d("TAG_FIREBASE", e.getMessage());
                })
                .addOnSuccessListener(documentSnapshot ->  {
                    this.currentUser=documentSnapshot.toObject(Workmate.class);
                    if(this.currentUser.getChosenRestaurantoday()!=null&&this.currentUser.getSettings().getNotificationsOn()) {
                        sendNotification();
                    }
                });
    }

    /**Sends a notification to the phone**/

    private void sendNotification(){

        /*Creates the notification builder*/

        NotificationCompat.Builder notificationBuilder;

        /*If the current SDK is Oreo or higher, then creates a chanel*/

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel
                    (CHANEL_NOTIFICATION, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANEL_NAME);
            NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            notificationBuilder=new NotificationCompat.Builder(this, CHANEL_NOTIFICATION);
        }

        /*Else just creates the notification builder and sets the priority*/

        else{
            notificationBuilder=new NotificationCompat.Builder(this);
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_DEFAULT);
        }

        /*Sets notification contents*/

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle(getString(R.string.app_name));
        String notificationText=getString(R.string.notification_lunch_text)+" "+this.currentUser.getChosenRestaurantoday().getName();
        notificationBuilder.setContentText(notificationText);
        notificationBuilder.setContentIntent(setRestaurantIntent());

        /*Sends the notification*/

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    /**Sets the restaurant intent to be opened when the user clicks on the notification*/

    protected PendingIntent setRestaurantIntent(){
        Intent restaurantActivityIntent = new Intent(this, RestaurantActivity.class);
        restaurantActivityIntent.putExtra(MainActivity.KEY_BUNDLE_USER, this.currentUser);
        restaurantActivityIntent.putExtra(MainActivity.KEY_BUNDLE_RESTAURANT, this.currentUser.getChosenRestaurantoday());
        return PendingIntent.getActivity(
                this, MainActivity.KEY_REQUEST_RESTAURANT, restaurantActivityIntent, PendingIntent.FLAG_ONE_SHOT);
    }
}
