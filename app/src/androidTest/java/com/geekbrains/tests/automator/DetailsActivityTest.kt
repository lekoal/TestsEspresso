package com.geekbrains.tests.automator

import FAKE_FLAVOR
import FAKE_TEST_NUMBER
import FAKE_TEST_NUMBER_DECREMENT
import FAKE_TEST_NUMBER_INCREMENT
import FakeDataSetup
import REAL_DEFAULT_ZERO_TEST_DECREMENT
import REAL_DEFAULT_ZERO_TEST_NUMBER
import REAL_DEFAULT_ZERO_TEST_NUMBER_INCREMENT
import TIMEOUT
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
        val detailsButton = uiDevice.findObject(
            By.res(packageName, "toDetailsActivityButton")
        )
        if (BuildConfig.FLAVOR == FAKE_FLAVOR) {
            FakeDataSetup(uiDevice, context)
        }
        detailsButton.click()
        uiDevice.wait(Until.hasObject(By.res(packageName, "decrementButton")), TIMEOUT)
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
        if (BuildConfig.FLAVOR == FAKE_FLAVOR) {
            assertEquals(detailsText, FAKE_TEST_NUMBER)
        } else {
            assertEquals(detailsText, REAL_DEFAULT_ZERO_TEST_NUMBER)
        }
    }

    @Test
    fun decrementButton_IsCorrect() {
        val detailsText = uiDevice.findObject(
            By.res(packageName, "totalCountTextView")
        )
        val decrementButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        if (BuildConfig.FLAVOR == FAKE_FLAVOR) {
            assertEquals(detailsText.text, FAKE_TEST_NUMBER)
        } else {
            assertEquals(detailsText.text, REAL_DEFAULT_ZERO_TEST_NUMBER)
        }
        decrementButton.click()
        if (BuildConfig.FLAVOR == FAKE_FLAVOR) {
            assertEquals(detailsText.text, FAKE_TEST_NUMBER_DECREMENT)
        } else {
            assertEquals(detailsText.text, REAL_DEFAULT_ZERO_TEST_DECREMENT)
        }
    }

    @Test
    fun incrementButton_IsCorrect() {
        val detailsText = uiDevice.findObject(
            By.res(packageName, "totalCountTextView")
        )
        val incrementButton = uiDevice.findObject(By.res(packageName, "incrementButton"))
        if (BuildConfig.FLAVOR == FAKE_FLAVOR) {
            assertEquals(detailsText.text, FAKE_TEST_NUMBER)
        } else {
            assertEquals(detailsText.text, REAL_DEFAULT_ZERO_TEST_NUMBER)
        }
        incrementButton.click()
        if (BuildConfig.FLAVOR == FAKE_FLAVOR) {
            assertEquals(detailsText.text, FAKE_TEST_NUMBER_INCREMENT)
        } else {
            assertEquals(detailsText.text, REAL_DEFAULT_ZERO_TEST_NUMBER_INCREMENT)
        }
    }
}