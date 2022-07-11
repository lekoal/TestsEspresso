package com.geekbrains.tests.automator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.geekbrains.tests.test.BuildConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class DetailsActivityTest {
    private val uiDevice: UiDevice = UiDevice.getInstance(
        InstrumentationRegistry.getInstrumentation()
    )
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val packageName = context.packageName

    @Before
    fun setUp() {
        uiDevice.pressHome()
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        val detailsButton = uiDevice.findObject(
            By.res(packageName, "toDetailsActivityButton")
        )
        editText.text = "opel"
        searchButton.click()
        uiDevice.wait(Until.hasObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        detailsButton.click()
        uiDevice.wait(Until.hasObject(By.res(packageName, "decrementButton")), TIMEOUT)
    }

    companion object {
        private const val TIMEOUT = 5000L
    }

    @Test
    fun detailsActivity_IsStarted() {
        val decrementButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        assertNotNull(decrementButton)
    }

    @Test
    fun detailsActivity_CorrectStarting() {
        val detailsText = uiDevice.findObject(
            By.res(packageName, "totalCountTextView")
        ).text
        if (BuildConfig.FLAVOR == "fake") {
            assertEquals(detailsText, "Number of results: 42")
        } else {
            assertEquals(detailsText, "Number of results: 291")
        }
    }

    @Test
    fun decrementButton_IsCorrect() {
        val detailsText = uiDevice.findObject(
            By.res(packageName, "totalCountTextView")
        )
        val decrementButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        if (BuildConfig.FLAVOR == "fake") {
            assertEquals(detailsText.text, "Number of results: 42")
        } else {
            assertEquals(detailsText.text, "Number of results: 291")
        }
        decrementButton.click()
        if (BuildConfig.FLAVOR == "fake") {
            assertEquals(detailsText.text, "Number of results: 41")
        } else {
            assertEquals(detailsText.text, "Number of results: 290")
        }
    }

    @Test
    fun incrementButton_IsCorrect() {
        val detailsText = uiDevice.findObject(
            By.res(packageName, "totalCountTextView")
        )
        val incrementButton = uiDevice.findObject(By.res(packageName, "incrementButton"))
        if (BuildConfig.FLAVOR == "fake") {
            assertEquals(detailsText.text, "Number of results: 42")
        } else {
            assertEquals(detailsText.text, "Number of results: 291")
        }
        incrementButton.click()
        if (BuildConfig.FLAVOR == "fake") {
            assertEquals(detailsText.text, "Number of results: 43")
        } else {
            assertEquals(detailsText.text, "Number of results: 292")
        }
    }
}