package com.sildian.go4lunch.model

import com.google.android.gms.maps.model.LatLng
import com.sildian.go4lunch.model.api.GooglePlacesDetailsResponse
import com.sildian.go4lunch.model.api.GooglePlacesSearchResponse
import com.sildian.go4lunch.model.api.HerePlacesResponse
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito
import java.util.*

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
        assertEquals(2, restaurant.getNbStars())
    }

    @Test
    fun give_FakeDetails_when_addDetails_then_checkDetailsReceived(){

        val open: GooglePlacesDetailsResponse.Result.OpeningHours.Period.Open=
                Mockito.mock(GooglePlacesDetailsResponse.Result.OpeningHours.Period.Open::class.java)
        Mockito.`when`(open.day).thenReturn(2)
        Mockito.`when`(open.time).thenReturn("1200")

        val close:GooglePlacesDetailsResponse.Result.OpeningHours.Period.Close=
                Mockito.mock(GooglePlacesDetailsResponse.Result.OpeningHours.Period.Close::class.java)
        Mockito.`when`(close.day).thenReturn(2)
        Mockito.`when`(close.time).thenReturn("1345")

        val period:GooglePlacesDetailsResponse.Result.OpeningHours.Period=
                Mockito.mock(GooglePlacesDetailsResponse.Result.OpeningHours.Period::class.java)
        Mockito.`when`(period.open).thenReturn(open)
        Mockito.`when`(period.close).thenReturn(close)

        val periods=listOf(period)

        val openingHours:GooglePlacesDetailsResponse.Result.OpeningHours=
                Mockito.mock(GooglePlacesDetailsResponse.Result.OpeningHours::class.java)
        Mockito.`when`(openingHours.periods).thenReturn(periods)

        val apiRestaurant:GooglePlacesDetailsResponse.Result=Mockito.mock(GooglePlacesDetailsResponse.Result::class.java)
        Mockito.`when`(apiRestaurant.internationalPhoneNumber).thenReturn("01 02 03 04 05")
        Mockito.`when`(apiRestaurant.website).thenReturn("https://www.superresto.com/")
        Mockito.`when`(apiRestaurant.openingHours).thenReturn(openingHours)

        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), "1 rue Miam",2.5)
        restaurant.addDetails(apiRestaurant)
        assertEquals("0102030405", restaurant.phoneNumber)
        assertEquals("https://www.superresto.com/", restaurant.webUrl)
        assertEquals(1, restaurant.openingHours.size)
        assertEquals(2, restaurant.openingHours[0].day)
        assertEquals("1200", restaurant.openingHours[0].openTime)
        assertEquals("1345", restaurant.openingHours[0].closeTime)
    }

    @Test
    fun given_italian_when_addExtraDetails_then_checkCuisineTypeIsItalian(){

        val tag: HerePlacesResponse.Result.Item.Tag=Mockito.mock(HerePlacesResponse.Result.Item.Tag::class.java)
        Mockito.`when`(tag.title).thenReturn("Italian")
        val tags=listOf(tag)

        val apiRestaurant:HerePlacesResponse.Result.Item=Mockito.mock(HerePlacesResponse.Result.Item::class.java)
        Mockito.`when`(apiRestaurant.tags).thenReturn(tags)

        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), "1 rue Miam",2.5)
        restaurant.addExtraDetails(apiRestaurant)
        assertEquals("Italian", restaurant.cuisineType)
    }

    @Test
    fun given_loc1_is_40_m5_loc2_is_41p261388_m3p3125_when_getDistanceInMeters_then_checkDistanceIs199872() {
        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), "1 rue Miam",2.5)
        val location=LatLng(41.261388, -3.3125)
        val distance:Int=restaurant.getDistanceInMeters(location)
        assertEquals(199872, distance)
    }

    @Test
    fun given_MondayAt11am_when_getCurrentOpeningHours_then_check_currentOpeningHoursClosed(){

        val calendar= Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, 2)
        calendar.set(Calendar.HOUR_OF_DAY, 11)
        calendar.set(Calendar.MINUTE, 0)

        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), "1 rue Miam",2.5)
        restaurant.openingHours.add(Restaurant.Period(0, "1200", "1500"))
        restaurant.openingHours.add(Restaurant.Period(2, "1200", "1345"))
        restaurant.openingHours.add(Restaurant.Period(2, "1930", "2145"))

        val currentOpeningHours:Restaurant.Period?=restaurant.getCurrentOpeningHours(calendar)
        assertNotNull(currentOpeningHours)
        if(currentOpeningHours!=null) {
            assertEquals(-1, currentOpeningHours.day)
        }
    }

    @Test
    fun given_TuesdayAt11am_when_getCurrentOpeningHours_then_check_currentOpeningHoursAre1200to1345(){

        val calendar= Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, 3)
        calendar.set(Calendar.HOUR_OF_DAY, 11)
        calendar.set(Calendar.MINUTE, 0)

        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), "1 rue Miam",2.5)
        restaurant.openingHours.add(Restaurant.Period(0, "1200", "1500"))
        restaurant.openingHours.add(Restaurant.Period(2, "1930", "2145"))
        restaurant.openingHours.add(Restaurant.Period(2, "1200", "1345"))

        val currentOpeningHours:Restaurant.Period?=restaurant.getCurrentOpeningHours(calendar)
        assertNotNull(currentOpeningHours)
        if(currentOpeningHours!=null) {
            assertEquals(2, currentOpeningHours.day)
            assertEquals("1200", currentOpeningHours.openTime)
            assertEquals("1345", currentOpeningHours.closeTime)
        }
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
}