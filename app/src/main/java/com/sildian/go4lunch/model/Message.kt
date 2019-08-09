package com.sildian.go4lunch.model

import java.util.*

/*************************************************************************************************
 * Message
 * Message sent in the chat
 ************************************************************************************************/

data class Message(
        val date: Date,                             //The message's date
        val workmate:Workmate,                      //The workmate who sent the message
        val text:String)                            //The text
{
    /**Empty constructor allowing to create a new instance from Firebase result**/

    constructor():this(Calendar.getInstance().time, Workmate(), "")
}