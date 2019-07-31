package com.sildian.go4lunch.model.firebase

/*************************************************************************************************
 * FirebaseRestaurant
 * Allows to store restaurant's data within Firebase
 ************************************************************************************************/

data class FirebaseRestaurant (
        val name:String,                                //The name
        val nbLikes:Int)                                //The number of likes given by workmates