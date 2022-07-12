import android.content.Context
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

internal const val FAKE_TEST_NUMBER = "Number of results: 42"
internal const val FAKE_TEST_NUMBER_INCREMENT = "Number of results: 43"
internal const val FAKE_TEST_NUMBER_DECREMENT = "Number of results: 41"
internal const val REAL_DEFAULT_ZERO_TEST_NUMBER = "Number of results: 0"
internal const val REAL_DEFAULT_ZERO_TEST_NUMBER_INCREMENT = "Number of results: 1"
internal const val REAL_DEFAULT_ZERO_TEST_DECREMENT = "Number of results: -1"
internal const val TIMEOUT = 5000L
internal const val FAKE_FLAVOR = "fake"
internal const val FAKE_SEARCH_TEXT = "sample"

internal class FakeDataSetup(
    uiDevice: UiDevice,
    context: Context
) {
    private val packageName: String = context.packageName
    private val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))!!
    private val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))!!

    init {
        editText.text = FAKE_SEARCH_TEXT
        searchButton.click()
        uiDevice.wait(
            Until.findObject(
            By.res(packageName, "totalCountTextView")), TIMEOUT)
    }
}
