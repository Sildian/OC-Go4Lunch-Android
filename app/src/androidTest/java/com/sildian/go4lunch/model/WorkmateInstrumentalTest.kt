package com.sildian.go4lunch.model

import android.os.Parcel
import org.junit.Assert.*
import org.junit.Test

class WorkmateInstrumentalTest{

    @Test
    fun given_MauriceJody_when_createFromParcel_then_checkResultIsMauriceJody(){

        val firstWorkmate=Workmate("W1", "Maurice Jody", null)
        val restaurant=Restaurant(
                "R1","Miam miam", 40.0, -5.0, "1 rue Miam",2.5)
        firstWorkmate.addLike(restaurant)
        firstWorkmate.updateLunch(restaurant)
        firstWorkmate.settings.searchRadius=500
        firstWorkmate.settings.notificationsOn=false

        val parcel: Parcel = Parcel.obtain()
        firstWorkmate.writeToParcel(parcel, firstWorkmate.describeContents())
        parcel.setDataPosition(0)

        val secondWorkmate=Workmate.createFromParcel(parcel)

        assertEquals("Maurice Jody", secondWorkmate.name)
        assertEquals(firstWorkmate, secondWorkmate)
        assertEquals(firstWorkmate.likes[0], secondWorkmate.likes[0])
        assertEquals(firstWorkmate.lunches[0], secondWorkmate.lunches[0])
        assertEquals(500, secondWorkmate.settings.searchRadius)
        assertFalse(secondWorkmate.settings.notificationsOn)
    }
}