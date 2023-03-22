package com.example.cupcake

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import com.example.cupcake.StartFragment
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTests {

    // NOTE: Unlike a NavHostController instance that a NavHostFragment would use,
    // TestNavHostController does not trigger the underlying navigate() behavior
    // (such as the FragmentTransaction that FragmentNavigator does) when you call navigate().
    // It only updates the state of the TestNavHostController.
    @Test
    fun navigation_StartFragment_to_FlavorFragment() {
        // Create a TestNavHostController
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        // Create a graphical FragmentScenario for the StartFragment
        val startFragmentScenario: FragmentScenario<StartFragment> = launchFragmentInContainer(themeResId = R.style.Theme_Cupcake)

        startFragmentScenario.onFragment { fragment ->
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.nav_graph)
            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Verify that performing a click changes the NavController’s state
        onView(withId(R.id.order_one_cupcake)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.flavorFragment)
    }

    @Test
    fun navigation_FlavorFragment_to_PickupFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val flavorFragmentScenario: FragmentScenario<FlavorFragment> = launchFragmentInContainer(themeResId = R.style.Theme_Cupcake)

        flavorFragmentScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            // Set the current destination to the FlavorFragment
            navController.setCurrentDestination(R.id.flavorFragment)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.next_button)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.pickupFragment)
    }

    @Test
    fun navigation_PickupFragment_to_SummaryFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val flavorFragmentScenario: FragmentScenario<PickupFragment> = launchFragmentInContainer(themeResId = R.style.Theme_Cupcake)

        flavorFragmentScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            // Set the current destination to the PickupFragment
            navController.setCurrentDestination(R.id.pickupFragment)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.next_button)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.summaryFragment)
    }

    @Test
    fun navigation_SummaryFragment_to_StartFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val flavorFragmentScenario: FragmentScenario<SummaryFragment> = launchFragmentInContainer(themeResId = R.style.Theme_Cupcake)

        flavorFragmentScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            // Set the current destination to the StartFragment
            navController.setCurrentDestination(R.id.summaryFragment)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.cancel_button)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.startFragment)
    }

}