package com.sildian.go4lunch.model

import com.google.android.gms.maps.model.LatLng
import org.junit.Test

import org.junit.Assert.*

class WorkmateTest {

    @Test
    fun given_MiamMiam_when_addLike_then_checkLikesContainsMiamMiam() {
        val workmate=Workmate("W1", "Maurice", "Jody", null)
        val restaurant=Restaurant("R1", "R1", "Miam miam", LatLng(10.0, 8.0), null,
                null, "1 rue Miam", null, null, "Toujours ouvert", 2.5)
        workmate.addLike(restaurant)
        assertEquals("Miam miam", workmate.likes[0].name)
        assertTrue(workmate.likes.contains(restaurant))
    }

    @Test
    fun given_MiamMiam_when_updateLunch_then_checkLunchRestaurantIsMiamMiamAndContainsMauriceJody() {
        val workmate = Workmate("W1", "Maurice", "Jody", null)
        val restaurant = Restaurant("R1", "R1", "Miam miam", LatLng(10.0, 8.0), null,
                null, "1 rue Miam", null, null, "Toujours ouvert", 2.5)
        workmate.updateLunch(restaurant)
        assertNotNull(workmate.lunchRestaurant)
        val workmateRestaurant:Restaurant?=workmate.lunchRestaurant
        if (workmateRestaurant != null) {
            assertEquals("Miam miam", workmateRestaurant.name)
        }
        assertEquals(restaurant, workmate.lunchRestaurant)
        assertEquals("Maurice", restaurant.lunchWorkmates[0].firstName)
        assertEquals("Jody", restaurant.lunchWorkmates[0].lastName)
        assertTrue(restaurant.lunchWorkmates.contains(workmate))
    }
}