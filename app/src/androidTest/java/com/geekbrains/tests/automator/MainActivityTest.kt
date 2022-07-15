package com.geekbrains.tests.automator

import FAKE_FLAVOR
import FAKE_TEST_NUMBER
import FakeDataSetup
import REAL_DEFAULT_ZERO_TEST_NUMBER
import TIMEOUT
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.geekbrains.tests.test.BuildConfig
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class MainActivityTest {
    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val packageName = context.packageName

    @Before
    fun setUp() {
        uiDevice.pressHome()
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    @Test
    fun mainActivity_IsStarted() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        assertNotNull(editText)
    }

    @Test
    fun search_IsWorking() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        val button = uiDevice.findObject(By.res(packageName, "searchButton"))
        editText.text = "opel"
        button.click()
        val textView = uiDevice.wait(
            Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT
        )
        if (BuildConfig.FLAVOR == FAKE_FLAVOR) {
            assertEquals(textView.text, FAKE_TEST_NUMBER)
        } else {
            assertEquals(textView.text, "Number of results: 292")
        }
    }

    @Test
    fun editText_TextChanging() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        val testText = "test text"
        editText.text = testText
        assertEquals(editText.text, testText)
    }

    @Test
    fun searchButton_TextCheck() {
        val button = uiDevice.findObject(By.res(packageName, "searchButton"))
        assertEquals(button.text, "SEARCH")
    }

    @Test
    fun toDetailsButton_TextCheck() {
        val button = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        assertEquals(button.text, "TO DETAILS")
    }

    @Test
    fun detailsActivity_CorrectOpening() {
        val toDetailsButton = uiDevice.findObject(
            By.res(packageName, "toDetailsActivityButton")
        )
        if (BuildConfig.FLAVOR == FAKE_FLAVOR) {
            FakeDataSetup(uiDevice, context)
        }
        toDetailsButton.click()
        uiDevice.wait(Until.hasObject(By.res(packageName, "decrementButton")), TIMEOUT)
        val detailsText = uiDevice.findObject(
            By.res(packageName, "totalCountTextView")
        ).text
        if (BuildConfig.FLAVOR == FAKE_FLAVOR) {
            assertEquals(detailsText, FAKE_TEST_NUMBER)
        } else {
            assertEquals(detailsText, REAL_DEFAULT_ZERO_TEST_NUMBER)
        }

    }

    @Test
    fun editText_EmptyFieldCheck() {
        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        searchButton.click()
        val mainText = uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        assertNull(mainText)
    }
}