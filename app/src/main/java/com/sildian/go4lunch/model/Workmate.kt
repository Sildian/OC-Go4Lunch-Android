package com.sildian.go4lunch.model

data class Workmate (
        val firebaseId:String,
        val firstName:String,
        val lastName:String,
        val imageUrl:String?)
{
    var likes=arrayListOf<Restaurant>();private set
    var lunchRestaurant:Restaurant?=null;private set

    constructor(firebaseId:String, firstName:String, lastName:String, imageUrl:String?,
                likes:ArrayList<Restaurant>, lunchRestaurant:Restaurant?):
            this(firebaseId, firstName, lastName, imageUrl){

        this.likes=likes
        this.lunchRestaurant=lunchRestaurant
    }

    fun addLike(restaurant:Restaurant){

    }

    fun updateLunch(restaurant:Restaurant?){

    }
}