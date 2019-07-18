package com.sildian.go4lunch.model

/*************************************************************************************************
 * Workmate
 * This class monitors the data related to a workmate
 ************************************************************************************************/

data class Workmate (
        val firebaseId:String,                              //The id given by Firebase
        val firstName:String,                               //The first name
        val lastName:String,                                //The last name
        val imageUrl:String?)                               //The image's url
{
    var likes=arrayListOf<Restaurant>();private set         //The list of liked restaurants
    var lunchRestaurant:Restaurant?=null;private set        //The restaurant where the workmate eats today

    /**This constructor allows to fill all fields**/

    constructor(firebaseId:String, firstName:String, lastName:String, imageUrl:String?,
                likes:ArrayList<Restaurant>, lunchRestaurant:Restaurant?):
            this(firebaseId, firstName, lastName, imageUrl){

        this.likes=likes
        this.lunchRestaurant=lunchRestaurant
    }

    /**Adds a restaurant to the list of liked restaurants
     * @Param restaurant : the liked restaurant
     */

    fun addLike(restaurant:Restaurant){
        if(!this.likes.contains(restaurant)){
            this.likes.add(restaurant)
        }
    }

    /**Updates the restaurant where the workmate eats today
     * @Param restaurant : the restaurant where the workmate eats today
     */

    fun updateLunch(restaurant:Restaurant?){

        /*If lunchRestaurant is not null, then indicates to the restaurant that the workmate doesn't eat here anymore*/

        var rest:Restaurant?=this.lunchRestaurant
        if(rest!=null){
            rest.updateLunch(this, false)
        }

        /*Updates lunchRestaurant*/

        this.lunchRestaurant=restaurant

        /*At last, indicates to the new restaurant that the workmate eats here*/

        rest=this.lunchRestaurant
        if(rest!=null){
            rest.updateLunch(this, true)
        }
    }
}