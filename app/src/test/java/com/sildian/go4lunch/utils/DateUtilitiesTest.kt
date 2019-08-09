package com.sildian.go4lunch.utils

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class DateUtilitiesTest{

    @Test
    fun given_1145_when_convertFormat_then_checkReturn11pp45(){
        val inputFormat="HHmm"
        val outputFormat="HH:mm"
        val inputTime="1145"
        val outputTime=DateUtilities.convertFormat(inputFormat, outputFormat, inputTime)
        assertEquals("11:45", outputTime)
    }

    @Test
    fun given_9Aug2019At11h45_when_convertFormat_then_checkReturn9Aug2019At11h45(){
        val calendar= Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2019)
        calendar.set(Calendar.MONTH, 7)
        calendar.set(Calendar.DAY_OF_MONTH, 9)
        calendar.set(Calendar.HOUR_OF_DAY, 11)
        calendar.set(Calendar.MINUTE, 45)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val date=calendar.time
        val displayDate=DateUtilities.convertFormat(date, "dd/MM/yyyy hh:mm")
        assertEquals("09/08/2019 11:45", displayDate)
    }
}