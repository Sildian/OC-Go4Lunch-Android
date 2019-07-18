package com.sildian.go4lunch.model

import com.google.android.gms.maps.model.LatLng

data class Restaurant (
        val placeId:String,
        val firebaseId:String?,
        val name:String,
        val location: LatLng,
        val imageUrl:String?,
        val cuisineType:String?,
        val adress:String,
        val phoneNumber:String?,
        val webUrl:String?,
        val openingHours:String,
        val score:Double)
{
    var nbLikes:Int=0;private set
    var lunchWorkmates=arrayListOf<Workmate>();private set

    constructor(placeId:String, firebaseId:String?, name:String, location:LatLng, imageUrl:String?,
                cuisineType:String?, adress:String, phoneNumber:String?, webUrl:String?, openingHours:String, score:Double,
                nbLikes:Int, lunchWorkmates:ArrayList<Workmate>):
            this(placeId, firebaseId, name, location, imageUrl, cuisineType, adress, phoneNumber, webUrl, openingHours, score){

        this.nbLikes=nbLikes
        this.lunchWorkmates=lunchWorkmates
    }

    fun getDistanceInMeters(userLocation:LatLng):Int{
        return 0
    }

    fun getNbStars():Int{
        return 0
    }

    fun increaseNbLikes(){

    }

    fun updateLunch(workmate:Workmate, eatsHere:Boolean){

    }
}