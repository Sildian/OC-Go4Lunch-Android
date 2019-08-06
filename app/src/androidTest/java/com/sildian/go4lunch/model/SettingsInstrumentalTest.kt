package com.sildian.go4lunch.model

import android.os.Parcel
import org.junit.Assert.*
import org.junit.Test

class SettingsInstrumentalTest{

    @Test
    fun given_radius500_notifications_off_when_createFromParcel_then_checkResultIsRadius500AndNotificationsOff(){

        val firstSettings=Settings(500, false)
        val parcel: Parcel = Parcel.obtain()
        firstSettings.writeToParcel(parcel, firstSettings.describeContents())
        parcel.setDataPosition(0)

        val secondSettings=Settings.createFromParcel(parcel)
        assertEquals(500, secondSettings.searchRadius)
        assertFalse(secondSettings.notificationsOn)
    }
}