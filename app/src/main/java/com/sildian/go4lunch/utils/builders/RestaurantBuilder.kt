package com.sildian.go4lunch.utils.builders

import com.google.android.gms.maps.model.LatLng
import com.sildian.go4lunch.model.Restaurant
import com.sildian.go4lunch.model.api.GooglePlacesDetailsResponse
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse
import com.sildian.go4lunch.model.api.HerePlacesResponse

/**************************************************************************************************
 * RestaurantBuilder
 * This class provides with methods allowing to build a Restaurant instance with a result received
 * from the api or Firebase
 *************************************************************************************************/

class RestaurantBuilder {

    companion object{

        /**Builds a Restaurant instance with an result received from GooglePlacesSearch API
         * @Param apiRestaurant : the result received from the api
         * @Param apiKey : the api key, allowing to build the photo reference
         * @return a Restaurant instance
         */

        fun buildRestaurant(apiRestaurant:GooglePlacesSearchResponse.Result, apiKey:String): Restaurant {

            /*Simple params*/

            val placeId:String=apiRestaurant.placeId.toString()
            val name:String=apiRestaurant.name.toString()
            val address:String=apiRestaurant.vicinity.toString()

            /*Location*/

            val location:LatLng

            if(apiRestaurant.geometry!=null&&apiRestaurant.geometry.location!=null
                &&apiRestaurant.geometry.location.lat!=null&&apiRestaurant.geometry.location.lng!=null) {
                location = LatLng(apiRestaurant.geometry.location.lat.toDouble(),
                        apiRestaurant.geometry.location.lng.toDouble())
            }else{
                location=LatLng(0.0, 0.0)
            }

            /*Image Url*/

            val imageUrl:String?
            if(apiRestaurant.photos!=null&&apiRestaurant.photos.size>0) {
                imageUrl = "Https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                        apiRestaurant.photos[0].photoReference + "&key=" + apiKey
            }else{
                imageUrl=null
            }

            /*Score*/

            val score: Double
            if(apiRestaurant.rating!=null) {
                score = apiRestaurant.rating.toDouble()
            }else{
                score=0.0
            }

            /*Then creates and returns the Restaurant instance*/

            return Restaurant(placeId, name, location, imageUrl, address, score)
        }

        fun addRestaurantDetails(apiRestaurant:GooglePlacesDetailsResponse.Result, restaurant:Restaurant){

        }

        fun addRestaurantExtraDetails(apiRestaurant:HerePlacesResponse.Result, restaurant:Restaurant){

        }
    }
}