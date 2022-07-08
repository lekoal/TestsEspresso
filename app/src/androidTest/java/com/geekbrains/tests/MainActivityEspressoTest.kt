package com.geekbrains.tests

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.search.MainActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        if (BuildConfig.TYPE == MainActivity.FAKE) {
            onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 42")))
        } else {
            onView(isRoot()).perform(delay())
            onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 2283")))
        }
    }

    private fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $2 seconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(2000)
            }
        }
    }

    @Test
    fun activitySearch_EditText_IsVisible() {
        onView(withId(R.id.searchEditText))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun activitySearch_EditText_IsDisplayed() {
        onView(withId(R.id.searchEditText))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun activitySearch_EditText_TextChanging() {
        val testText = "test text"
        onView(withId(R.id.searchEditText))
            .perform(click())
        onView(withId(R.id.searchEditText))
            .perform(replaceText(testText), closeSoftKeyboard())
        onView(withId(R.id.searchEditText))
            .check(matches(withText(testText)))
    }

    @Test
    fun activitySearch_Button_IsVisible() {
        onView(withId(R.id.toDetailsActivityButton))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun activitySearch_Button_IsDisplayed() {
        onView(withId(R.id.toDetailsActivityButton))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun activitySearch_Button_TextCheck() {
        onView(withId(R.id.toDetailsActivityButton))
            .check(matches(withText("to details")))
    }

    @Test
    fun activitySearch_Button_ClickSuccess() {
        onView(withId(R.id.toDetailsActivityButton)).perform(click())
        assertNotEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activitySearch_RecyclerView_IsVisible() {
        onView(withId(R.id.recyclerView))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @After
    fun close() {
        scenario.close()
    }
}
