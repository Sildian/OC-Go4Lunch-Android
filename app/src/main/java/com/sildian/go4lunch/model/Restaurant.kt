package com.sildian.go4lunch.model

import com.google.android.gms.maps.model.LatLng
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToLong
import kotlin.math.sin

/*************************************************************************************************
 * Restaurant
 * This class monitors the data related to a restaurant
 ************************************************************************************************/

data class Restaurant (
        val placeId:String,                         //The place id returned by Google Places API
        val name:String,                            //The name
        val location: LatLng,                       //The location
        val imageUrl:String?,                       //The image's url
        val address:String,                         //The address
        val score:Double)                           //The score given by Google's users
{
    var firebaseId:String?=null                     //The id given by Firebase after it is stored
    var phoneNumber:String?=null                    //The phone number
    var webUrl:String?=null                         //The web url
    var openingHours:String?=null                   //The opening hours
    var cuisineType:String?=null                    //The cuisine type
    var nbLikes:Int=0                               //The number of likes given by the workmates
    val lunchWorkmates=arrayListOf<Workmate>()      //The workmates to eat in this restaurant today

    /**Distance between the restaurant and the user
     * @Param userLocation : the user location
     * @return the distance in meters
     */

    fun getDistanceInMeters(userLocation:LatLng):Int{
        val earthRadius=6371.0
        val arg1:Double= sin(Math.toRadians(this.location.latitude)) * sin(Math.toRadians(userLocation.latitude))
        val arg2:Double= cos(Math.toRadians(this.location.latitude)) * cos(Math.toRadians(userLocation.latitude)) *
                cos(Math.toRadians(userLocation.longitude-this.location.longitude))
        val distanceInKMeters:Double=earthRadius* acos(arg1+arg2)
        return (distanceInKMeters*1000).toInt()
    }

    /**Calculates the number of stars
     * using the Google's score on 5 and given that the max number of stars is 3
     * @return the number of stars
     */

    fun getNbStars():Int{
        return (score / 5 * 3).roundToLong().toInt()
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