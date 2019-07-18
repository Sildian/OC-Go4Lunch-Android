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
        if(!this.likes.contains(restaurant)){
            this.likes.add(restaurant)
        }
    }

    fun updateLunch(restaurant:Restaurant?){
        var rest:Restaurant?=this.lunchRestaurant
        if(rest!=null){
            rest.updateLunch(this, false)
        }
        this.lunchRestaurant=restaurant
        rest=this.lunchRestaurant
        if(rest!=null){
            rest.updateLunch(this, true)
        }
    }
}