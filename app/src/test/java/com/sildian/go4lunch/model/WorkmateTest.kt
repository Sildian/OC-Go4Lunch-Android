package com.sildian.go4lunch.model

import org.junit.Test

import org.junit.Assert.*

class WorkmateTest {

    @Test
    fun given_MiamMiam_when_addLike_then_checkLikesContainsMiamMiam() {
        val workmate=Workmate("W1", "Maurice Jody", null)
        val restaurant=Restaurant(
                "R1","Miam miam", 40.0, -5.0, "1 rue Miam",2.5)
        workmate.addLike(restaurant)
        assertEquals("Miam miam", workmate.likes[0].name)
        assertTrue(workmate.likes.contains(restaurant))
        assertEquals(1, restaurant.nbLikes)
    }

    @Test
    fun given_MiamMiam_and_SuperPizza_when_updateLunch_then_checkLunchRestaurantIsSuperPizza() {

        val workmate = Workmate("W1", "Maurice Jody", null)
        val restaurant = Restaurant(
                "R1", "Miam miam", 40.0, -5.0, "1 rue Miam", 2.5)
        val secondRestaurant = Restaurant(
                "R2", "Super pizza", 42.0, -3.0, "1 rue Pizza", 3.5)
        workmate.updateLunch(restaurant)
        workmate.updateLunch(secondRestaurant)
        assertEquals(1, workmate.lunches.size)

        val workmateRestaurant: Restaurant = workmate.lunches[0].restaurant
        assertEquals("Super pizza", workmateRestaurant.name)
        assertEquals(secondRestaurant, workmateRestaurant)
    }

    @Test
    fun given_nothing_when_getChosenRestaurantToday_then_checkResponseIsnull(){
        val workmate = Workmate("W1", "Maurice Jody", null)
        assertNull(workmate.getChosenRestaurantoday())
    }

    @Test
    fun given_MiamMiam_And_SuperPizza_when_getChosenRestaurantToday_then_checkResponseIsSuperPizza(){
        val workmate = Workmate("W1", "Maurice Jody", null)
        val restaurant = Restaurant(
                "R1", "Miam miam", 40.0, -5.0, "1 rue Miam", 2.5)
        val secondRestaurant = Restaurant(
                "R2", "Super pizza", 42.0, -3.0, "1 rue Pizza", 3.5)
        workmate.updateLunch(restaurant)
        workmate.updateLunch(secondRestaurant)

        val chosenRestaurant=workmate.getChosenRestaurantoday()
        assertNotNull(chosenRestaurant)
        if(chosenRestaurant!=null){
            assertEquals("Super pizza", chosenRestaurant.name)
        }
    }
}