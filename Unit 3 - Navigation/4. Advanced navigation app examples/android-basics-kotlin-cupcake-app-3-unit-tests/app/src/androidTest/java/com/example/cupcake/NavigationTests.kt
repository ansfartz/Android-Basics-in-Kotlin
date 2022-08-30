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

    @Test
    fun navigation_fromStartFragment_toFlavorFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val startFragmentScenario: FragmentScenario<StartFragment> = launchFragmentInContainer(themeResId = R.style.Theme_Cupcake)

        startFragmentScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.order_one_cupcake)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.flavorFragment)
    }

}