package com.sildian.go4lunch.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*************************************************************************************************
 * GooglePlacesSearchResponse
 * This class and its nested class host the data received from GooglePlacesSearch API
 ************************************************************************************************/

class GooglePlacesSearchResponse {

    @SerializedName("html_attributions") @Expose val htmlAttributions: List<Any>? = null
    @SerializedName("next_page_token") @Expose val nextPageToken: String? = null
    @SerializedName("results") @Expose val results: List<Result>? = null

    class Result {

        @SerializedName("geometry") @Expose val geometry: Geometry? = null
        @SerializedName("icon") @Expose val icon: String? = null
        @SerializedName("id") @Expose val id: String? = null
        @SerializedName("name") @Expose val name: String? = null
        @SerializedName("opening_hours") @Expose val openingHours: OpeningHours? = null
        @SerializedName("photos") @Expose val photos: List<Photo>? = null
        @SerializedName("place_id") @Expose val placeId: String? = null
        @SerializedName("plus_code") @Expose val plusCode: PlusCode? = null
        @SerializedName("rating") @Expose val rating: Double? = null
        @SerializedName("reference") @Expose val reference: String? = null
        @SerializedName("scope") @Expose val scope: String? = null
        @SerializedName("types") @Expose val types: List<String>? = null
        @SerializedName("user_ratings_total") @Expose val userRatingsTotal: Int? = null
        @SerializedName("vicinity") @Expose val vicinity: String? = null
        @SerializedName("price_level") @Expose val priceLevel: Int? = null

        class Geometry {

            @SerializedName("location") @Expose val location: Location? = null
            @SerializedName("viewport") @Expose val viewport: Viewport? = null

            class Location {

                @SerializedName("lat") @Expose val lat:Double?=null
                @SerializedName("lng") @Expose val lng:Double?=null
            }

            class Viewport {

                @SerializedName("northeast") @Expose val northeast:Northeast?=null
                @SerializedName("southwest") @Expose val southwest:Southwest?=null

                class Northeast {

                    @SerializedName("lat") @Expose val lat:Double?=null
                    @SerializedName("lng") @Expose val lng:Double?=null
                }

                class Southwest {

                    @SerializedName("lat") @Expose val lat:Double?=null
                    @SerializedName("lng") @Expose val lng:Double?=null
                }
            }
        }

        class OpeningHours {

            @SerializedName("open_now") @Expose val openNow:Boolean?=null
        }

        class Photo {

            @SerializedName("height") @Expose val height:Int?=null
            @SerializedName("html_attributions") @Expose val htmlAttributions:List<String>? = null
            @SerializedName("photo_reference") @Expose val photoReference:String?=null
            @SerializedName("width") @Expose val width:Int?=null
        }

        class PlusCode {

            @SerializedName("compound_code") @Expose val compoundCode:String?=null
            @SerializedName("global_code") @Expose val globalCode:String?=null
        }
    }
}