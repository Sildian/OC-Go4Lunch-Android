package com.sildian.go4lunch.model

import android.os.Parcel
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.*
import org.junit.Test

class RestaurantInstrumentalTest{

    @Test
    fun given_Miam_when_createFromParcel_then_checkResultIsMiam(){

        val firstRestaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), "1 rue Miam",2.5)
        val parcel: Parcel = Parcel.obtain()
        firstRestaurant.writeToParcel(parcel, firstRestaurant.describeContents())
        parcel.setDataPosition(0)

        val secondRestaurant=Restaurant.createFromParcel(parcel)

        assertEquals("Miam miam", secondRestaurant.name)
        assertEquals(firstRestaurant, secondRestaurant)
    }
}