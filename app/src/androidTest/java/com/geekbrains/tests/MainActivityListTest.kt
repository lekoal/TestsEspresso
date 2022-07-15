package com.geekbrains.tests

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.test.BuildConfig
import com.geekbrains.tests.view.search.MainActivity
import com.geekbrains.tests.view.search.SearchResultAdapter
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityListTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
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
    fun mainActivity_testScrollTo() {
        if (BuildConfig.FLAVOR == "real") {
            onView(withId(R.id.searchEditText)).perform(click())
            onView(withId(R.id.searchEditText)).perform(
                replaceText("tts"),
                closeSoftKeyboard()
            )
            onView(withId(R.id.searchButton)).perform(click())
            onView(isRoot()).perform(delay())
            onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                    hasDescendant(withText("jscrane/TTS"))
                )
            )
        }
    }

    @Test
    fun mainActivity_testClickAtFirstPosition() {
        if (BuildConfig.FLAVOR == "real") {
            onView(withId(R.id.searchEditText)).perform(click())
            onView(withId(R.id.searchEditText)).perform(
                replaceText("tts"),
                closeSoftKeyboard()
            )
            onView(withId(R.id.searchButton)).perform(click())
            onView(isRoot()).perform(delay())
            onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                        0,
                        click()
                    )
            )
        }
    }

    @Test
    fun mainActivity_testClickAtHiddenPosition() {
        if (BuildConfig.FLAVOR == "real") {
            onView(withId(R.id.searchEditText)).perform(click())
            onView(withId(R.id.searchEditText)).perform(
                replaceText("tts"),
                closeSoftKeyboard()
            )
            onView(withId(R.id.searchButton)).perform(click())
            onView(isRoot()).perform(delay())
            onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                    hasDescendant(withText("petercunha/tts"))
                )
            )
            onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItem<SearchResultAdapter.SearchResultViewHolder>(
                    hasDescendant(withText("jscrane/TTS")),
                    click()
                )
            )
        }
    }

    private fun tapOnId(id: Int) = object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Нажатие на view с id: $id"
        }

        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById(id) as View
            v.performClick()
        }
    }

    @Test
    fun mainActivity_testCustomClick() {
        if (BuildConfig.FLAVOR == "real") {
            onView(withId(R.id.searchEditText)).perform(click())
            onView(withId(R.id.searchEditText)).perform(
                replaceText("tts"),
                closeSoftKeyboard()
            )
            onView(withId(R.id.searchButton)).perform(click())
            onView(isRoot()).perform(delay())
            onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItem<SearchResultAdapter.SearchResultViewHolder>(
                    hasDescendant(withText("jscrane/TTS")),
                    tapOnId(R.id.repositoryName)
                )
            )
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}