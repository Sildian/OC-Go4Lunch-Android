package com.sildian.go4lunch.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

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
    val lunches=arrayListOf<Lunch>()                         //The list of restaurants where a workmate eats

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

    fun updateLunch(restaurant:Restaurant):Boolean{

        /*Gets the date and creates a LunchRestaurant*/

        val calendar=Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val date=calendar.time
        val lunch= Lunch(date, restaurant)

        /*If the lunch is not already contained in the lunches...*/

        if(!this.lunches.contains(lunch)) {

            /*If a lunch already exists for the current date, then removes it*/

            var idToRemove:Int?=null
            for(item in this.lunches){
                if(item.date.equals(date)){
                    idToRemove=this.lunches.indexOf(item)
                }
            }
            if(idToRemove!=null) {
                this.lunches.removeAt(idToRemove)
            }

            /*Then adds the new lunch*/

            this.lunches.add(lunch)

            return true
        }

        else return false
    }

    /**This nested class provides with data grouping a date and a restaurant for each lunch**/

    data class Lunch(val date: Date, val restaurant:Restaurant)
}