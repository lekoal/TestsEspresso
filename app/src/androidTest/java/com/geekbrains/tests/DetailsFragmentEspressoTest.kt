package com.geekbrains.tests

import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.details.DetailsFragment
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsFragmentEspressoTest {

    private lateinit var scenario: FragmentScenario<DetailsFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer()
    }

    @Test
    fun detailsFragment_testAssertNotNull() {
        scenario.onFragment {
            assertNotNull(it)
        }
    }

    @Test
    fun detailsFragment_textView_testNotNull() {
        scenario.onFragment {
            val textView = it.view?.findViewById<TextView>(R.id.totalCountTextView)
            assertNotNull(textView)
        }
    }

    @Test
    fun detailsFragment_textView_testIsDisplayed() {
        onView(withId(R.id.totalCountTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun detailsFragment_textView_testIsCompletelyDisplayed() {
        onView(withId(R.id.totalCountTextView)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun detailsFragment_buttons_testAreEffectiveVisible() {
        onView(withId(R.id.incrementButton))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.decrementButton))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun detailsFragment_testBundle() {
        val fragmentArgs = bundleOf("TOTAL_COUNT_EXTRA" to 15)
        val scenario = launchFragmentInContainer<DetailsFragment>(fragmentArgs)
        scenario.moveToState(Lifecycle.State.RESUMED)

        val assertion = matches(withText("Number of results: 15"))
        onView(withId(R.id.totalCountTextView)).check(assertion)
    }

    @Test
    fun detailsFragment_testCountChanging() {
        scenario.onFragment {
            it.setCount(22)
        }
        onView(withId(R.id.totalCountTextView))
            .check(matches(withText("Number of results: 22")))
    }

    @Test
    fun detailsFragment_buttonIncrement_testIsWorking() {
        onView(withId(R.id.incrementButton)).perform(ViewActions.click())
        onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 1")))
    }

    @Test
    fun detailsFragment_buttonDecrement_testIsWorking() {
        onView(withId(R.id.decrementButton)).perform(ViewActions.click())
        onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: -1")))
    }
}