package com.sildian.go4lunch.model

import android.os.Parcel
import android.os.Parcelable
import com.sildian.go4lunch.utils.DateUtilities
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
    val lunches=arrayListOf<Lunch>()                        //The list of restaurants where a workmate eats
    var settings=Settings()                                 //The user settings

    /**Empty constructor allowing to create a new instance from Firebase result**/

    constructor():this("", "", "")

    /**Constructor (Parcelable)**/

    constructor(parcel: Parcel):this(parcel.readString(), parcel.readString(), parcel.readString()){
        this.likes.addAll(parcel.createTypedArray(Restaurant.CREATOR))
        this.lunches.addAll(parcel.createTypedArray(Lunch.CREATOR))
        this.settings=parcel.readParcelable(Settings::class.java.classLoader)
    }

    /**Parcelable**/

    override fun writeToParcel(parcel: Parcel, flags:Int){
        parcel.writeString(this.firebaseId)
        parcel.writeString(this.name)
        parcel.writeString(this.imageUrl)
        parcel.writeTypedArray(this.likes.toTypedArray(), flags)
        parcel.writeTypedArray(this.lunches.toTypedArray(), flags)
        parcel.writeParcelable(this.settings, flags)
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

    /**Overrides equals so the only criteria is the firebaseId**/

    override fun equals(other: Any?): Boolean {
        if(other!=null&&other.javaClass==Workmate::class.java){
            val secondWorkmate=other as Workmate
            return secondWorkmate.firebaseId.equals(this.firebaseId)
        }else {
            return false
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

        val date=DateUtilities.getDate()
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

    /**Gets the restaurant where the workmate eats today
     * @Return the restaurant if it exists, else return null
     */

    fun getChosenRestaurantoday():Restaurant?{

        val date=DateUtilities.getDate()

        for(item in this.lunches){
            if(item.date.equals(date)){
                return item.restaurant
            }
        }
        return null
    }

    /**This nested class provides with data grouping a date and a restaurant for each lunch**/

    data class Lunch(val date: Date, val restaurant:Restaurant):Parcelable{

        /**Empty constructor allowing to create a new instance from Firebase result**/

        constructor():this(Calendar.getInstance().time, Restaurant())

        /**Constructor (Parcelable)**/

        constructor(parcel: Parcel): this(
                Date(parcel.readLong()),
                parcel.readParcelable(Restaurant::class.java.classLoader))

        /**Parcelable**/

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeLong(this.date.time)
            parcel.writeParcelable(this.restaurant, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR:Parcelable.Creator<Lunch>{
            override fun createFromParcel(parcel: Parcel): Lunch {
                return Lunch(parcel)
            }

            override fun newArray(size: Int): Array<Lunch?> {
                return arrayOfNulls(size)
            }
        }
    }

    /**This nested class registers the user settings**/

    data class Settings (
            var searchRadius:Int=1500,                      //The radius where a research is restricted from a given location
            var notificationsOn:Boolean=true)               //Activates or not daily notifications from Firebase
        : Parcelable{

        /**Constructor (parcelable)**/

        constructor(parcel: Parcel):this(){
            this.searchRadius=parcel.readInt()
            val notifications=parcel.readInt()
            this.notificationsOn=notifications>0
        }

        /**Parcelable**/

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(this.searchRadius)
            if(this.notificationsOn){
                parcel.writeInt(1)
            }
            else{
                parcel.writeInt(0)
            }
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR:Parcelable.Creator<Settings>{
            override fun createFromParcel(parcel: Parcel): Settings {
                return Settings(parcel)
            }

            override fun newArray(size: Int): Array<Settings?> {
                return arrayOfNulls(size)
            }
        }
    }
}