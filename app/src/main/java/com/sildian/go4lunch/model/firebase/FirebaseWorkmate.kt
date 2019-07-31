package com.sildian.go4lunch.model.firebase

/*************************************************************************************************
 * FirebaseWorkmate
 * Allows to store workmate's data within Firebase
 ************************************************************************************************/

data class FirebaseWorkmate (
        val firebaseId:String,                              //The id given by Firebase
        val name:String,                                    //The name
        val imageUrl:String?)                               //The image's url
{

    /**Equal depends on firebaseId**/

    override fun equals(other: Any?): Boolean {
        if(other!=null&&other.javaClass==FirebaseWorkmate::class.java){
            val workmate=other as FirebaseWorkmate
            return workmate.firebaseId==this.firebaseId
        }
        else return false
    }
}
