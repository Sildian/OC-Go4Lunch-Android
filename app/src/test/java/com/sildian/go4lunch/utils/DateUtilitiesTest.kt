package com.sildian.go4lunch.utils

import org.junit.Assert.*
import org.junit.Test

class DateUtilitiesTest{

    @Test
    fun given_1145_when_convertFormat_then_checkReturn1145(){
        val inputFormat="HHmm"
        val outputFormat="HH:mm"
        val inputTime="1145"
        val outputTime=DateUtilities.convertFormat(inputFormat, outputFormat, inputTime)
        assertEquals("11:45", outputTime)
    }
}