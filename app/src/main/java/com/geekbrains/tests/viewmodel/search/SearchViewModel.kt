package com.geekbrains.tests.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class SearchViewModel internal constructor(
    private val repository: RepositoryContract,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {
    private val _liveData = MutableLiveData<ScreenState>()
    fun subscribeToLiveData(): LiveData<ScreenState> = _liveData
    fun searchGitHub(searchQuery: String) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.searchGithub(searchQuery)
                .subscribeOn(schedulerProvider.background())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { _liveData.value = ScreenState.Loading }
                .subscribeWith(object : DisposableObserver<SearchResponse>() {
                    override fun onNext(t: SearchResponse) {
                        val searchResults = t.searchResults
                        val totalCount = t.totalCount
                        if (searchResults != null && totalCount != null) {
                            _liveData.value = ScreenState.Working(t)
                        } else {
                            _liveData.value =
                                ScreenState.Error(
                                    Throwable("Search results or total count are null")
                                )
                        }
                    }

                    override fun onError(e: Throwable) {
                        _liveData.value = ScreenState.Error(
                            Throwable(e.message ?: "Response is null or unsuccessful")
                        )
                    }

                    override fun onComplete() {
                    }
                })
        )
    }
}

sealed class ScreenState {
    object Loading : ScreenState()
    data class Working(val searchResponse: SearchResponse) : ScreenState()
    data class Error(val error: Throwable) : ScreenState()
}