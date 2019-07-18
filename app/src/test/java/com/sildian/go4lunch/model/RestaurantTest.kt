package com.sildian.go4lunch.model

import com.google.android.gms.maps.model.LatLng
import org.junit.Test

import org.junit.Assert.*

class RestaurantTest {

    @Test
    fun given_loc1_is_40_m5_loc2_is_41p261388_m3p3125_when_getDistanceInMeters_then_checkDistanceIs199872() {
        var restaurant:Restaurant=Restaurant("R1", "R1", "Miam miam", LatLng(40.0, -5.0), null,
                null, "1 rue Miam", null, null, "Toujours ouvert", 2.5)
        val location:LatLng=LatLng(41.261388, -3.3125)
        val distance:Int=restaurant.getDistanceInMeters(location)
        assertEquals(199872, distance)
    }

    @Test
    fun given_scoreIs2p5_when_getNbStars_then_checkNbStarsIs2() {
        var restaurant:Restaurant=Restaurant("R1", "R1", "Miam miam", LatLng(10.0, 8.0), null,
                null, "1 rue Miam", null, null, "Toujours ouvert", 2.5)
        val nbStars:Int=restaurant.getNbStars()
        assertEquals(2, nbStars)
    }

    @Test
    fun given_1like_when_increaseNbLikes_then_checkNbLikesIs1() {
        var restaurant:Restaurant=Restaurant("R1", "R1", "Miam miam", LatLng(10.0, 8.0), null,
                null, "1 rue Miam", null, null, "Toujours ouvert", 2.5)
        restaurant.increaseNbLikes()
        assertEquals(1, restaurant.nbLikes)
    }

    @Test
    fun given_addMauriceJody_when_updateLunch_then_checkLunchWorkmatesContainsMauriceJody() {
        var restaurant:Restaurant=Restaurant("R1", "R1", "Miam miam", LatLng(10.0, 8.0), null,
                null, "1 rue Miam", null, null, "Toujours ouvert", 2.5)
        var workmate:Workmate=Workmate("W1", "Maurice", "Jody", null)
        restaurant.updateLunch(workmate, true)
        assertEquals("Maurice", restaurant.lunchWorkmates.get(0).firstName)
        assertEquals("Jody", restaurant.lunchWorkmates.get(0).lastName)
        assertTrue(restaurant.lunchWorkmates.contains(workmate))
    }

    @Test
    fun given_addAndRemoveMauriceJody_when_updateLunch_then_checkLunchWorkmatesDoesNotContainMauriceJody() {
        var restaurant:Restaurant=Restaurant("R1", "R1", "Miam miam", LatLng(10.0, 8.0), null,
                null, "1 rue Miam", null, null, "Toujours ouvert", 2.5)
        var workmate:Workmate=Workmate("W1", "Maurice", "Jody", null)
        restaurant.updateLunch(workmate, true)
        restaurant.updateLunch(workmate, false)
        assertFalse(restaurant.lunchWorkmates.contains(workmate))
    }
}