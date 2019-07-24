package com.sildian.go4lunch.model

import com.google.android.gms.maps.model.LatLng
import org.junit.Test

import org.junit.Assert.*

class RestaurantTest {

    @Test
    fun given_loc1_is_40_m5_loc2_is_41p261388_m3p3125_when_getDistanceInMeters_then_checkDistanceIs199872() {
        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), null,
                "1 rue Miam",2.5)
        val location=LatLng(41.261388, -3.3125)
        val distance:Int=restaurant.getDistanceInMeters(location)
        assertEquals(199872, distance)
    }

    @Test
    fun given_scoreIs2p5_when_getNbStars_then_checkNbStarsIs2() {
        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), null,
                "1 rue Miam",2.5)
        val nbStars:Int=restaurant.getNbStars()
        assertEquals(2, nbStars)
    }

    @Test
    fun given_1like_when_increaseNbLikes_then_checkNbLikesIs1() {
        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), null,
                "1 rue Miam",2.5)
        restaurant.increaseNbLikes()
        assertEquals(1, restaurant.nbLikes)
    }

    @Test
    fun given_addMauriceJody_when_updateLunch_then_checkLunchWorkmatesContainsMauriceJody() {
        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), null,
                "1 rue Miam",2.5)
        val workmate=Workmate("W1", "Maurice", "Jody", null)
        restaurant.updateLunch(workmate, true)
        assertEquals("Maurice", restaurant.lunchWorkmates[0].firstName)
        assertEquals("Jody", restaurant.lunchWorkmates[0].lastName)
        assertTrue(restaurant.lunchWorkmates.contains(workmate))
    }

    @Test
    fun given_addAndRemoveMauriceJody_when_updateLunch_then_checkLunchWorkmatesDoesNotContainMauriceJody() {
        val restaurant=Restaurant("R1","Miam miam", LatLng(40.0, -5.0), null,
                "1 rue Miam",2.5)
        val workmate=Workmate("W1", "Maurice", "Jody", null)
        restaurant.updateLunch(workmate, true)
        restaurant.updateLunch(workmate, false)
        assertFalse(restaurant.lunchWorkmates.contains(workmate))
    }
}