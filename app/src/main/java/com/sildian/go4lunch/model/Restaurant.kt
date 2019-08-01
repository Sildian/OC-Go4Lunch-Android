package com.sildian.go4lunch.model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

import com.sildian.go4lunch.model.api.GooglePlacesDetailsResponse
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse
import com.sildian.go4lunch.model.api.HerePlacesResponse
import java.util.*

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
        val address:String,                         //The address
        val score:Double?)                          //The score given by Google's users
    : Parcelable
{
    var phoneNumber:String?=null                    //The phone number
    var webUrl:String?=null                         //The web url
    var openingHours=arrayListOf<Period>()          //The opening hours for each day of the week
    var cuisineType:String?=null                    //The cuisine type
    var nbLikes:Int=0                               //The number of likes given by the workmates

    /**Empty constructor allowing to create a new instance from Firebase result**/

    constructor():this("", "", LatLng(0.0, 0.0), "", null)

    /**Constructor called to build a Restaurant from Google Places API's result
     * @Param apiRestaurant :  : the result received from the api
     */

    constructor(apiRestaurant: GooglePlacesSearchResponse.Result):
            this(apiRestaurant.placeId.toString(),
                    apiRestaurant.name.toString(),
                    if(apiRestaurant.geometry?.location?.lat != null &&apiRestaurant.geometry.location.lng!=null)
                        LatLng(apiRestaurant.geometry.location.lat.toDouble(), apiRestaurant.geometry.location.lng.toDouble())
                    else LatLng(0.0, 0.0) ,
                    apiRestaurant.vicinity.toString(),
                    apiRestaurant.rating)

    /**Constructor (Parcelable)**/

    constructor(parcel: Parcel):
            this(parcel.readString(), parcel.readString(), parcel.readParcelable(LatLng::class.java.classLoader),
                    parcel.readString(), parcel.readDouble())

    /**Parcelable**/

    override fun writeToParcel(parcel:Parcel, flags: Int) {
        parcel.writeString(this.placeId)
        parcel.writeString(this.name)
        parcel.writeParcelable(this.location, flags)
        parcel.writeString(this.address)
        parcel.writeDouble(if(this.score!=null)this.score.toDouble() else 0.0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR:Parcelable.Creator<Restaurant>{
        override fun createFromParcel(parcel: Parcel): Restaurant {
            return Restaurant(parcel)
        }

        override fun newArray(size: Int): Array<Restaurant?> {
            return arrayOfNulls(size)
        }
    }

    /**Adds details from Google Places Details API's result
     * @Param apiRestaurant : the result received from the api
     */

    fun addDetails(apiRestaurant: GooglePlacesDetailsResponse.Result){
        this.phoneNumber=
                if(apiRestaurant.internationalPhoneNumber!=null)
                    apiRestaurant.internationalPhoneNumber.replace(" ", "")
                else null
        this.webUrl=apiRestaurant.website
        if(apiRestaurant.openingHours!=null&&apiRestaurant.openingHours.periods!=null) {
            for(item in apiRestaurant.openingHours.periods){
                if(item.open!=null&&item.close!=null&&item.open.day!=null) {
                    val day = item.open.day
                    val openTime = item.open.time
                    val closeTime = item.close.time
                    val period=Period(day.toInt(), openTime.toString(), closeTime.toString())
                    this.openingHours.add(period)
                }
            }
        }
    }

    /**Adds extra details from Here Places API's result
     * @Param apiRestaurant : the result received from the api
     */

    fun addExtraDetails(apiRestaurant: HerePlacesResponse.Result.Item){
        if(apiRestaurant.tags!=null&& apiRestaurant.tags.isNotEmpty()){
            this.cuisineType=
                    if(apiRestaurant.tags.size>1)apiRestaurant.tags[1].title
                    else apiRestaurant.tags[0].title
        }
    }

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

    /**Gets the current opening hours
     * @Param calendar : the calendar
     */

    fun getCurrentOpeningHours(calendar: Calendar):Period?{

        /*Sets the current day and time with a format matching the periods contents*/

        val currentDay=calendar.get(Calendar.DAY_OF_WEEK)-1
        val currentTime:Int=(calendar.get(Calendar.HOUR_OF_DAY).toString()+calendar.get(Calendar.MINUTE).toString()).toInt()

        /*If opening hours are empty, return null*/

        if(openingHours.isEmpty()){
            return null
        }else {

            /*Gets all opening periods matching the current day*/

            val matchingPeriods=arrayListOf<Period>()
            for(item in openingHours){
                if(item.day==currentDay){
                    matchingPeriods.add(item)
                }
            }

            /*If no matching period exist, return a period with day -1 indicating that the restaurant is closed*/

            if (matchingPeriods.isEmpty()){
                return Period(-1, "", "")

                /*If one matching period exists, return this period*/

            }else if(matchingPeriods.size==1){
                return Period(matchingPeriods[0].day, matchingPeriods[0].openTime, matchingPeriods[0].closeTime)

                /*If several matching periods exist, return the next period to the current time*/

            }else{
                fun selector(p: Period): Int = p.openTime.toInt()
                matchingPeriods.sortBy { selector(it) }
                for(item in matchingPeriods){
                    if(item.openTime.toInt()>=currentTime||item.closeTime.toInt()>=currentTime){
                        return item
                    }
                }
                return Period(-1, "", "")
            }
        }
    }

    /**Calculates the number of stars
     * using the Google's score on 5 and given that the max number of stars is 3
     * @return the number of stars
     */

    fun getNbStars():Int{
        return if(score!=null)(score / 5 * 3).roundToLong().toInt() else 0
    }

    /**Increases the number of likes by 1**/

    fun increaseNbLikes(){
        this.nbLikes++
    }

    /**This nested class provides with periods allowing to know the opening hours for each day of the week**/

    data class Period (val day:Int, val openTime:String, val closeTime:String){
        constructor():this(0, "", "")
    }
}