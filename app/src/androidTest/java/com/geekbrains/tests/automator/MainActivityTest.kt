package com.geekbrains.tests.automator

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class MainActivityTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val packageName = context.packageName

    @Test
    fun search_IsWorking() {

    }
}