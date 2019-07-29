package com.sildian.go4lunch.utils

import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/*************************************************************************************************
 * DateUtilities
 * This class provides with static methods to convert date and time formats
 ************************************************************************************************/

class DateUtilities {

    companion object {

        /**Gets the local time format pattern
         * @return a String containing the local time format's pattern
         */

        fun getLocalTimeFormatPattern(): String {
            val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
            val simpleTimeFormat = timeFormat as SimpleDateFormat
            return simpleTimeFormat.toPattern()
        }

        /**Converts a date or a time's format
         * @param inputFormat : the input format
         * @param outputFormat : the output format
         * @param inputItem : the input date or time
         * @return the output date or time
         */

        fun convertFormat(inputFormat: String, outputFormat: String, inputItem: String): String? {

            var outputItem: String? = null

            if (!inputItem.isEmpty()) {

                val simpleInputFormat = SimpleDateFormat(inputFormat)
                val simpleOutputFormat = SimpleDateFormat(outputFormat)

                val date:Date
                try {
                    date = simpleInputFormat.parse(inputItem)
                } catch (e: ParseException) {
                    Log.d("TAG_DATE", e.message)
                    return null
                }

                outputItem = simpleOutputFormat.format(date)
            }

            return outputItem
        }
    }
}