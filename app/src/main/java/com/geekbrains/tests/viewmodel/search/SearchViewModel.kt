package com.geekbrains.tests.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.presenter.RepositoryContract
import kotlinx.coroutines.*

class SearchViewModel internal constructor(private val repository: RepositoryContract) :
    ViewModel() {
    private val _liveData = MutableLiveData<ScreenState>()
    fun subscribeToLiveData(): LiveData<ScreenState> = _liveData
    private val scope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        }
    )

    fun searchGitHub(searchQuery: String) {
        _liveData.value = ScreenState.Loading
        scope.launch {
            val searchResponse = repository.searchGithubAsync(searchQuery)
            val searchResults = searchResponse.searchResults
            val totalCount = searchResponse.totalCount
            if (searchResults != null && totalCount != null) {
                _liveData.value = ScreenState.Working(searchResponse)
            } else {
                _liveData.value = ScreenState.Error(
                    Throwable("Search results or total count are null")
                )
            }
        }
    }

    private fun handleError(error: Throwable) {
        _liveData.value = ScreenState.Error(
            Throwable(error.message ?: "Response is null or unsuccessful")
        )
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}

sealed class ScreenState {
    object Loading : ScreenState()
    data class Working(val searchResponse: SearchResponse) : ScreenState()
    data class Error(val error: Throwable) : ScreenState()
}