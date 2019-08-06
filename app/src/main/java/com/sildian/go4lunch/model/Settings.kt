package com.sildian.go4lunch.model

import android.os.Parcel
import android.os.Parcelable

/*************************************************************************************************
 * Settings
 * This class registers the user settings
 ************************************************************************************************/

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