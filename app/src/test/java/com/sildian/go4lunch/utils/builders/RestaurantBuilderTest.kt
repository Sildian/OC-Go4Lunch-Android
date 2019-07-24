package com.sildian.go4lunch.utils.builders

import com.sildian.go4lunch.model.Restaurant
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class RestaurantBuilderTest{

    @Test
    fun given_Miam_when_buildRestaurant_then_checkResultIsMiam(){

        val apiRestaurantGeometryLocation:GooglePlacesSearchResponse.Result.Geometry.Location=
                Mockito.mock(GooglePlacesSearchResponse.Result.Geometry.Location::class.java)
        Mockito.`when`(apiRestaurantGeometryLocation.lat).thenReturn(25.15)
        Mockito.`when`(apiRestaurantGeometryLocation.lng).thenReturn(12.78)

        val apiRestaurantGeometry:GooglePlacesSearchResponse.Result.Geometry=
                Mockito.mock(GooglePlacesSearchResponse.Result.Geometry::class.java)
        Mockito.`when`(apiRestaurantGeometry.location).thenReturn(apiRestaurantGeometryLocation)

        val apiRestaurantPhoto:GooglePlacesSearchResponse.Result.Photo=
                Mockito.mock(GooglePlacesSearchResponse.Result.Photo::class.java)
        Mockito.`when`(apiRestaurantPhoto.photoReference).thenReturn("MiamPhoto")
        val apiRestaurantPhotos=listOf(apiRestaurantPhoto)

        val apiRestaurant: GooglePlacesSearchResponse.Result=
                Mockito.mock(GooglePlacesSearchResponse.Result::class.java)
        Mockito.`when`(apiRestaurant.placeId).thenReturn("Fake id")
        Mockito.`when`(apiRestaurant.name).thenReturn("Miam")
        Mockito.`when`(apiRestaurant.geometry).thenReturn(apiRestaurantGeometry)
        Mockito.`when`(apiRestaurant.photos).thenReturn(apiRestaurantPhotos)
        Mockito.`when`(apiRestaurant.vicinity).thenReturn("1 rue Miam")
        Mockito.`when`(apiRestaurant.rating).thenReturn(2.5)

        val restaurant: Restaurant = RestaurantBuilder.buildRestaurant(apiRestaurant, "FakeApiKey")
        assertEquals("Fake id", restaurant.placeId)
        assertEquals("Miam", restaurant.name)
        assertEquals(25.15, restaurant.location.latitude, 0.0)
        assertEquals(12.78, restaurant.location.longitude, 0.0)
        assertEquals("Https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=MiamPhoto&key=FakeApiKey",
                restaurant.imageUrl)
        assertEquals("1 rue Miam", restaurant.address)
        assertEquals(2.5, restaurant.score, 0.0)
    }
}