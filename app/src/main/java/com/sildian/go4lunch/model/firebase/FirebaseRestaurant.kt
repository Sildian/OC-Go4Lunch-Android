package com.sildian.go4lunch.model.firebase

/*************************************************************************************************
 * FirebaseRestaurant
 * Allows to store restaurant's data within Firebase
 ************************************************************************************************/

data class FirebaseRestaurant (
        val placeId:String,                             //The Google place id
        val name:String,                                //The name
        val nbLikes:Int)                                //The number of likes given by workmates
{

    /**Equal depends on placeId**/

    override fun equals(other: Any?): Boolean {
        if(other!=null&&other.javaClass==FirebaseRestaurant::class.java){
            val restaurant=other as FirebaseRestaurant
            return restaurant.placeId==this.placeId
        }
        else return false
    }
}