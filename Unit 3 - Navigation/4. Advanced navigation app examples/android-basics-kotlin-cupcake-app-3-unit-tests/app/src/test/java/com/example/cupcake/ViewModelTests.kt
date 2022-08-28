package com.example.cupcake

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cupcake.model.OrderViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ViewModelTests {

    /**
     * In the context of an Android app, think of the main thread as the UI thread, the code that shows the UI to a user runs on this thread.
     * Unless otherwise specified, a unit test assumes that everything runs on the main thread.
     * However, because LiveData objects cannot access the main thread we have to explicitly state that LiveData objects should not call the main thread.
     */

    /**
     * To specify that LiveData objects should not call the main thread we need to provide a specific test rule any time we are testing a LiveData object.
     */
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun quantity_twelve_cupcakes() {
        val viewModel = OrderViewModel()
        viewModel.quantity.observeForever {}
        viewModel.setQuantity(12)

        assertEquals(12, viewModel.quantity.value)
    }

    @Test
    fun price_twelve_cupcakes() {
        val viewModel = OrderViewModel()

        /**
         * Without the below line line, the test will fail. The test result says that our actual value was null.
         * There is an explanation for this. If you look at the price variable in OrderViewModel you will see this:
         *
         *      val price: LiveData<String> = Transformations.map(_price) {
         *      // Format the price into the local currency and return this as LiveData<String>
         *      NumberFormat.getCurrencyInstance().format(it)
         *      }
         *
         * This is an example of why LiveData should be observed in testing. The value of price is set by using a Transformation.
         * Essentially, this code takes the value that we assign to price and transforms it to a currency format so we don't have to do it manually.
         *
         * However, this code has other implications. When transforming a LiveData object, the code doesn't get called unless it absolutely has to be, this saves resources on a mobile device.
         * The code will only be called if we observe the object for changes. Of course, this is done in our app, but we also need to do the same for the test.
         */
        viewModel.price.observeForever {}


        viewModel.setQuantity(12)
        assertEquals("$27.00", viewModel.price.value)
    }

}