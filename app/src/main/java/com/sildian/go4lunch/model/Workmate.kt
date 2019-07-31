package com.sildian.go4lunch.model

import android.os.Parcel
import android.os.Parcelable

/*************************************************************************************************
 * Workmate
 * This class monitors the data related to a workmate
 ************************************************************************************************/

data class Workmate(
        val firebaseId:String,                              //The id given by Firebase
        val name:String,                                    //The name
        val imageUrl:String?)                               //The image's url
    :Parcelable
{
    val likes=arrayListOf<Restaurant>()                     //The list of liked restaurants
    var lunchRestaurant:Restaurant?=null;private set        //The restaurant where the workmate eats today

    /**Constructor (Parcelable)**/

    constructor(parcel: Parcel):this(parcel.readString(), parcel.readString(), parcel.readString())

    /**Parcelable**/

    override fun writeToParcel(parcel: Parcel, flags:Int){
        parcel.writeString(this.firebaseId)
        parcel.writeString(this.name)
        parcel.writeString(this.imageUrl)
    }

    override fun describeContents():Int{
        return 0
    }

    companion object CREATOR:Parcelable.Creator<Workmate>{
        override fun createFromParcel(parcel: Parcel): Workmate {
            return Workmate(parcel)
        }

        override fun newArray(size: Int): Array<Workmate?> {
            return arrayOfNulls(size)
        }
    }

    /**Adds a restaurant to the list of liked restaurants
     * @Param restaurant : the liked restaurant
     * @Return true if a like was added, false otherwise
     */

    fun addLike(restaurant:Restaurant):Boolean{
        if(!this.likes.contains(restaurant)){
            this.likes.add(restaurant)
            restaurant.increaseNbLikes()
            return true
        }
        else return false
    }

    /**Updates the restaurant where the workmate eats today
     * @Param restaurant : the restaurant where the workmate eats today
     * @Return true if a new lunch is created, false otherwise
     */

    fun updateLunch(restaurant:Restaurant?):Boolean{

        var rest:Restaurant?=this.lunchRestaurant

        if(restaurant!=null&&(rest==null||!rest.placeId.equals(restaurant.placeId))) {

            /*indicates to the restaurant that the workmate doesn't eat here anymore*/

            if(rest!=null) {
                rest.updateLunch(this, false)
            }

            /*Updates lunchRestaurant*/

            this.lunchRestaurant = restaurant

            /*At last, indicates to the new restaurant that the workmate eats here*/

            rest = this.lunchRestaurant
            if (rest != null) {
                rest.updateLunch(this, true)
            }
            return true
        }
        else return false
    }
}