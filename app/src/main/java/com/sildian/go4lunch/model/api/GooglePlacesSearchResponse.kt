package com.sildian.go4lunch.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GooglePlacesSearchResponse {

    @SerializedName("html_attributions") @Expose val htmlAttributions: List<Any>? = null
    @SerializedName("next_page_token") @Expose val nextPageToken: String? = null
    @SerializedName("results") @Expose val results: List<GooglePlacesSearchResult>? = null
}