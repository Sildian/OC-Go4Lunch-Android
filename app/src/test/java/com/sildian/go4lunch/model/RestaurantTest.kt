package com.sildian.go4lunch.model

import com.google.android.gms.maps.model.LatLng
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito

class RestaurantTest {

    @Test
    fun given_Miam_when_newRestaurantFromGooglePlacesSearchAPI_then_checkResultIsMiam(){

        val apiRestaurantGeometryLocation: GooglePlacesSearchResponse.Result.Geometry.Location=
                Mockito.mock(GooglePlacesSearchResponse.Result.Geometry.Location::class.java)
        Mockito.`when`(apiRestaurantGeometryLocation.lat).thenReturn(25.15)
        Mockito.`when`(apiRestaurantGeometryLocation.lng).thenReturn(12.78)

        val apiRestaurantGeometry: GooglePlacesSearchResponse.Result.Geometry=
                Mockito.mock(GooglePlacesSearchResponse.Result.Geometry::class.java)
        Mockito.`when`(apiRestaurantGeometry.location).thenReturn(apiRestaurantGeometryLocation)

        val apiRestaurant: GooglePlacesSearchResponse.Result=
                Mockito.mock(GooglePlacesSearchResponse.Result::class.java)
        Mockito.`when`(apiRestaurant.placeId).thenReturn("Fake id")
        Mockito.`when`(apiRestaurant.name).thenReturn("Miam")
        Mockito.`when`(apiRestaurant.geometry).thenReturn(apiRestaurantGeometry)
        Mockito.`when`(apiRestaurant.vicinity).thenReturn("1 rue Miam")
        Mockito.`when`(apiRestaurant.rating).thenReturn(2.5)

        val restaurant = Restaurant(apiRestaurant)
        assertEquals("Fake id", restaurant.placeId)
        assertEquals("Miam", restaurant.name)
        assertEquals(25.15, restaurant.location.latitude, 0.0)
        assertEquals(12.78, restaurant.location.longitude, 0.0)
        assertEquals("1 rue Miam", restaurant.address)
        assertEquals(2.5, restaurant.score, 0.0)
    }

    @Test
    fun given_loc1_is_40_m5_loc2_is_41p261388_m3p3125_when_getDistanceInMeters_then_checkDistanceIs199872() {
        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), "1 rue Miam",2.5)
        val location=LatLng(41.261388, -3.3125)
        val distance:Int=restaurant.getDistanceInMeters(location)
        assertEquals(199872, distance)
    }

    @Test
    fun given_scoreIs2p5_when_getNbStars_then_checkNbStarsIs2() {
        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), "1 rue Miam",2.5)
        val nbStars:Int=restaurant.getNbStars()
        assertEquals(2, nbStars)
    }

    @Test
    fun given_1like_when_increaseNbLikes_then_checkNbLikesIs1() {
        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), "1 rue Miam",2.5)
        restaurant.increaseNbLikes()
        assertEquals(1, restaurant.nbLikes)
    }

    @Test
    fun given_addMauriceJody_when_updateLunch_then_checkLunchWorkmatesContainsMauriceJody() {
        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), "1 rue Miam",2.5)
        val workmate=Workmate("W1", "Maurice Jody", null)
        restaurant.updateLunch(workmate, true)
        assertEquals("Maurice Jody", restaurant.lunchWorkmates[0].name)
        assertTrue(restaurant.lunchWorkmates.contains(workmate))
    }

    @Test
    fun given_addAndRemoveMauriceJody_when_updateLunch_then_checkLunchWorkmatesDoesNotContainMauriceJody() {
        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), "1 rue Miam",2.5)
        val workmate=Workmate("W1", "Maurice Jody", null)
        restaurant.updateLunch(workmate, true)
        restaurant.updateLunch(workmate, false)
        assertFalse(restaurant.lunchWorkmates.contains(workmate))
    }
}