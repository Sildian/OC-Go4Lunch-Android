package com.sildian.go4lunch.model

import android.os.Parcel
import org.junit.Assert.*
import org.junit.Test

class WorkmateInstrumentalTest{

    @Test
    fun given_MauriceJody_when_createFromParcel_then_checkResultIsMauriceJody(){

        val firstWorkmate=Workmate("W1", "Maurice Jody", null)
        val parcel: Parcel = Parcel.obtain()
        firstWorkmate.writeToParcel(parcel, firstWorkmate.describeContents())
        parcel.setDataPosition(0)

        val secondWorkmate=Workmate.createFromParcel(parcel)

        assertEquals("Maurice Jody", secondWorkmate.name)
        assertEquals(firstWorkmate, secondWorkmate)
    }
}