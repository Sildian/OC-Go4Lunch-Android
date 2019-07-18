package com.sildian.go4lunch.model

import android.location.Location
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
        /*val distanceResults:FloatArray= floatArrayOf()
        Location.distanceBetween(this.location.latitude, this.location.longitude,
                userLocation.latitude, userLocation.longitude, distanceResults)
        val distance:Int= Math.round(distanceResults.get(distanceResults.size-1))*/
        val earthRadius:Int=6371
        val arg1:Double=Math.sin(this.location.latitude)*Math.sin(userLocation.latitude)
        val arg2:Double=Math.cos(this.location.latitude)*Math.cos(userLocation.latitude)*
                Math.cos(this.location.longitude-userLocation.longitude)
        val distanceInKMeters:Double=earthRadius*Math.acos(arg1+arg2)
        return (distanceInKMeters*1000).toInt()
    }

    fun getNbStars():Int{
        val nbStars:Int=Math.round(this.score/5*3).toInt()
        return nbStars
    }

    fun increaseNbLikes(){
        this.nbLikes++
    }

    fun updateLunch(workmate:Workmate, eatsHere:Boolean){
        when{
            !this.lunchWorkmates.contains(workmate)&&eatsHere->this.lunchWorkmates.add(workmate)
            this.lunchWorkmates.contains(workmate)&&!eatsHere->this.lunchWorkmates.remove(workmate)
        }
    }
}