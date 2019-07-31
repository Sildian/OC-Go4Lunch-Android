package com.sildian.go4lunch.model.firebase

/*************************************************************************************************
 * FirebaseLike
 * Allows to store like's data within Firebase
 ************************************************************************************************/

data class FirebaseLike(
        val restaurant: FirebaseRestaurant,
        val workmate: FirebaseWorkmate)