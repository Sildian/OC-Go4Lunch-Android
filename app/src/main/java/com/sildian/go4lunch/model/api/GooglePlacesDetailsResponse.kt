package com.sildian.go4lunch.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*************************************************************************************************
 * GooglePlacesSearchDetails
 * This class and its nested class host the data received from GooglePlacesDetails API
 ************************************************************************************************/

class GooglePlacesDetailsResponse {

    @SerializedName("html_attributions") @Expose val htmlAttributions: List<Any>? = null
    @SerializedName("result") @Expose val result: Result? = null
    @SerializedName("status") @Expose val status: String? = null

    class Result {

        @SerializedName("address_components") @Expose val addressComponents:List<AddressComponent>? = null
        @SerializedName("adr_address") @Expose val adrAddress:String?=null
        @SerializedName("formatted_address") @Expose val formattedAddress:String?=null
        @SerializedName("formatted_phone_number") @Expose val formattedPhoneNumber:String?=null
        @SerializedName("geometry") @Expose val geometry:Geometry?=null
        @SerializedName("icon") @Expose val icon:String?=null
        @SerializedName("id") @Expose val id:String?=null
        @SerializedName("international_phone_number") @Expose val internationalPhoneNumber:String?=null
        @SerializedName("name") @Expose val name:String?=null
        @SerializedName("opening_hours") @Expose val openingHours:OpeningHours?=null
        @SerializedName("photos") @Expose val photos:List<Photo>?=null
        @SerializedName("place_id") @Expose val placeId:String?=null
        @SerializedName("plus_code") @Expose val plusCode:PlusCode?=null
        @SerializedName("price_level") @Expose val priceLevel:Int?=null
        @SerializedName("rating") @Expose val rating:Double?=null
        @SerializedName("reference") @Expose val reference:String?=null
        @SerializedName("reviews") @Expose val reviews:List<Review>?=null
        @SerializedName("scope") @Expose val scope:String?=null
        @SerializedName("types") @Expose val types:List<String>?=null
        @SerializedName("url") @Expose val url:String?=null
        @SerializedName("user_ratings_total") @Expose val userRatingsTotal:Int?=null
        @SerializedName("utc_offset") @Expose val utcOffset:Int?=null
        @SerializedName("vicinity") @Expose val vicinity:String?=null
        @SerializedName("website") @Expose val website:String?=null

        class AddressComponent {

            @SerializedName("long_name") @Expose val longName:String?=null
            @SerializedName("short_name") @Expose val shortName:String?=null
            @SerializedName("types") @Expose val types:List<String>?=null
        }

        class Geometry {

            @SerializedName("location") @Expose val location:Location?=null
            @SerializedName("viewport") @Expose val viewport:Viewport?=null

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
    }

    class OpeningHours {

        @SerializedName("open_now") @Expose val openNow:Boolean?=null
        @SerializedName("periods") @Expose val periods:List<Period>?=null
        @SerializedName("weekday_text") @Expose val weekdayText:List<String>?=null

        class Period {

            @SerializedName("close") @Expose val close:Close?=null
            @SerializedName("open") @Expose val open:Open?=null

            class Close {

                @SerializedName("day") @Expose val day:Int?=null
                @SerializedName("time") @Expose val time:String?=null
            }

            class Open {

                @SerializedName("day") @Expose val day:Int?=null
                @SerializedName("time") @Expose val time:String?=null
            }
        }
    }

    class Photo {

        @SerializedName("height") @Expose val height:Int?=null
        @SerializedName("html_attributions") @Expose val htmlAttributions:List<String>?=null
        @SerializedName("photo_reference") @Expose val photoReference:String?=null
        @SerializedName("width") @Expose val width:Int?=null
    }

    class PlusCode {

        @SerializedName("compound_code") @Expose val compoundCode:String?=null
        @SerializedName("global_code") @Expose val globalCode:String?=null
    }

    class Review {

        @SerializedName("author_name") @Expose val authorName:String?=null
        @SerializedName("author_url") @Expose val authorUrl:String?=null
        @SerializedName("language") @Expose val language:String?=null
        @SerializedName("profile_photo_url") @Expose val profilePhotoUrl:String?=null
        @SerializedName("rating") @Expose val rating:Int?=null
        @SerializedName("relative_time_description") @Expose val relativeTimeDescription:String?=null
        @SerializedName("text") @Expose val text:String?=null
        @SerializedName("time") @Expose val time:Int?=null
    }
}