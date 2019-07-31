package com.sildian.go4lunch.model.firebase

import java.util.*

/*************************************************************************************************
 * FirebaseLunch
 * Allows to store lunch's data within Firebase
 ************************************************************************************************/

data class FirebaseLunch (
        val date: Date,                                 //The date of the lunch
        val restaurant:FirebaseRestaurant,              //The restaurant
        val workmate:FirebaseWorkmate)                  //The workmate