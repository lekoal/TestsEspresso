package com.geekbrains.tests.automator

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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

    companion object {
        private const val TIMEOUT = 5000L
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
        uiDevice.wait(Until.hasObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        val textView = uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        if (BuildConfig.FLAVOR == "fake") {
            assertEquals(textView.text, "Number of results: 42")
        } else {
            assertEquals(textView.text, "Number of results: 291")
        }
    }
}