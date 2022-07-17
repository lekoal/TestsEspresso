package com.geekbrains.tests

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.presenter.stubs.ScheduleProviderStub
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.viewmodel.search.ScreenState
import com.geekbrains.tests.viewmodel.search.SearchViewModel
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SearchViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: GitHubRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchViewModel = SearchViewModel(repository, ScheduleProviderStub())
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
        private const val ERROR_NULL_RESULT = "Search results or total count are null"
    }

    @Test
    fun search_Test() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    1,
                    listOf()
                )
            )
        )
        searchViewModel.searchGitHub(SEARCH_QUERY)
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)
    }

    @Test
    fun liveData_ReturnNotNull() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    1,
                    listOf()
                )
            )
        )
        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)
            assertNotNull(liveData.value)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun liveData_ReturnValueIsError() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()
        val error = Throwable(ERROR_TEXT)

        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.error(error)
        )
        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)
            val value: ScreenState.Error = liveData.value as ScreenState.Error
            assertEquals(value.error.message, error.message)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun liveData_IsLoading() {
        var state: ScreenState? = null
        val observer = Observer<ScreenState> {
            if (it == ScreenState.Loading) {
                state = it
            }
        }
        val liveData = searchViewModel.subscribeToLiveData()

        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    1,
                    listOf()
                )
            )
        )
        try {
            liveData.observeForever(observer)
            val loading = ScreenState.Loading
            searchViewModel.searchGitHub(SEARCH_QUERY)

            assertEquals(loading, state)

        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun liveData_ResultIsNull() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    null,
                    null
                )
            )
        )
        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)
            val error = Throwable(ERROR_NULL_RESULT)
            val value: ScreenState.Error = liveData.value as ScreenState.Error
            assertEquals(value.error.message, error.message)
        } finally {
            liveData.removeObserver(observer)
        }
    }
}