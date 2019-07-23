package com.sildian.go4lunch.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*************************************************************************************************
 * HerePlacesResponse
 * This class and its nested class host the data received from HerePlaces API
 ************************************************************************************************/

class HerePlacesResponse {

    @SerializedName("results") @Expose val results: Results? = null
    @SerializedName("search") @Expose val search: Search? = null

    class Results {

        @SerializedName("items") @Expose val items:List<Item>?=null

        class Item {

            @SerializedName("position") @Expose val position:List<Double>?=null
            @SerializedName("distance") @Expose val distance:Int?=null
            @SerializedName("title") @Expose val title:String?=null
            @SerializedName("averageRating") @Expose val averageRating:Int?=null
            @SerializedName("category") @Expose val category:Category?=null
            @SerializedName("icon") @Expose val icon:String?=null
            @SerializedName("vicinity") @Expose val vicinity:String?=null
            @SerializedName("having") @Expose val having:List<Object>?=null
            @SerializedName("type") @Expose val type:String?=null
            @SerializedName("href") @Expose val href:String?=null
            @SerializedName("tags") @Expose val tags:List<Tag>?=null
            @SerializedName("id") @Expose val id:String?=null
            @SerializedName("openingHours") @Expose val openingHours:OpeningHours?=null

            class Category {

                @SerializedName("id") @Expose val id:String?=null
                @SerializedName("title") @Expose val title:String?=null
                @SerializedName("href") @Expose val href:String?=null
                @SerializedName("type") @Expose val type:String?=null
                @SerializedName("system") @Expose val system:String?=null
            }

            class Tag {

                @SerializedName("id") @Expose val id:String?=null
                @SerializedName("title") @Expose val title:String?=null
                @SerializedName("group") @Expose val group:String?=null
            }

            class OpeningHours {

                @SerializedName("text") @Expose val text:String?=null
                @SerializedName("label") @Expose val label:String?=null
                @SerializedName("isOpen") @Expose val isOpen:Boolean?=null
                @SerializedName("structured") @Expose val structured:List<Structured>?=null

                class Structured {

                    @SerializedName("start") @Expose val start:String?=null
                    @SerializedName("duration") @Expose val duration:String?=null
                    @SerializedName("recurrence") @Expose val recurrence:String?=null
                }

            }
        }
    }

    class Search {

        @SerializedName("context") @Expose val context:Context?=null

        class Context {

            @SerializedName("location") @Expose val location:Location?=null
            @SerializedName("type") @Expose val type:String?=null
            @SerializedName("href") @Expose val href:String?=null

            class Location {

                @SerializedName("position") @Expose val position:List<Double>?=null
                @SerializedName("address") @Expose val address:Address?=null

                class Address {

                    @SerializedName("text") @Expose val text:String?=null
                    @SerializedName("house") @Expose val house:String?=null
                    @SerializedName("street") @Expose val street:String?=null
                    @SerializedName("postalCode") @Expose val postalCode:String?=null
                    @SerializedName("city") @Expose val city:String?=null
                    @SerializedName("county") @Expose val county:String?=null
                    @SerializedName("stateCode") @Expose val stateCode:String?=null
                    @SerializedName("country") @Expose val country:String?=null
                    @SerializedName("countryCode") @Expose val countryCode:String?=null
                }
            }
        }
    }
}