package com.sildian.go4lunch.model

import com.google.android.gms.maps.model.LatLng

/*************************************************************************************************
 * Restaurant
 * This class monitors the data related to a restaurant
 ************************************************************************************************/

data class Restaurant (
        val placeId:String,                         //The place id returned by Google Places API
        val firebaseId:String?,                     //The id given by Firebase after it is stored
        val name:String,                            //The name
        val location: LatLng,                       //The location
        val imageUrl:String?,                       //The image's url
        val cuisineType:String?,                    //The cuisine type
        val adress:String,                          //The adress
        val phoneNumber:String?,                    //The phone number
        val webUrl:String?,                         //The web url
        val openingHours:String,                    //The opening hours
        val score:Double)                           //The score given by Google's users
{
    var nbLikes:Int=0;private set                               //The number of likes given by the workmates
    var lunchWorkmates=arrayListOf<Workmate>();private set      //The workmates to eat in this restaurant today

    /**This constructor allows to fill all fields**/

    constructor(placeId:String, firebaseId:String?, name:String, location:LatLng, imageUrl:String?,
                cuisineType:String?, adress:String, phoneNumber:String?, webUrl:String?, openingHours:String, score:Double,
                nbLikes:Int, lunchWorkmates:ArrayList<Workmate>):
            this(placeId, firebaseId, name, location, imageUrl, cuisineType, adress, phoneNumber, webUrl, openingHours, score){

        this.nbLikes=nbLikes
        this.lunchWorkmates=lunchWorkmates
    }

    /**Distance between the restaurant and the user
     * @Param userLocation : the user location
     * @return the distance in meters
     */

    fun getDistanceInMeters(userLocation:LatLng):Int{
        val earthRadius:Double=6371.0
        val arg1:Double=Math.sin(Math.toRadians(this.location.latitude))*Math.sin(Math.toRadians(userLocation.latitude))
        val arg2:Double=Math.cos(Math.toRadians(this.location.latitude))*Math.cos(Math.toRadians(userLocation.latitude))*
                Math.cos(Math.toRadians(userLocation.longitude-this.location.longitude))
        val distanceInKMeters:Double=earthRadius*Math.acos(arg1+arg2)
        return (distanceInKMeters*1000).toInt()
    }

    /**Calculates the number of stars
     * using the Google's score on 5 and given that the max number of stars is 3
     * @return the number of stars
     */

    fun getNbStars():Int{
        val nbStars:Int=Math.round(this.score/5*3).toInt()
        return nbStars
    }

    /**Increases the number of likes by 1**/

    fun increaseNbLikes(){
        this.nbLikes++
    }

    /**Updates the list of workmates to eat to the restaurant today
     * @Param workmate : the workmate to add or remove
     * @Param eatsHere : indicates whether the workmate eats to the restaurant or not
     */

    fun updateLunch(workmate:Workmate, eatsHere:Boolean){
        when{
            !this.lunchWorkmates.contains(workmate)&&eatsHere->this.lunchWorkmates.add(workmate)
            this.lunchWorkmates.contains(workmate)&&!eatsHere->this.lunchWorkmates.remove(workmate)
        }
    }
}