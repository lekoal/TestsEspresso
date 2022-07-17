package com.geekbrains.tests.di

import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.repository.GitHubApi
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.utils.SchedulerProvider
import com.geekbrains.tests.viewmodel.search.SearchSchedulerProvider
import com.geekbrains.tests.viewmodel.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val repoModule = module {
    single(named("api_url")) { "https://api.github.com" }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(get<String>(named("api_url")))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<GitHubApi> {
        get<Retrofit>().create(GitHubApi::class.java)
    }
    single<RepositoryContract>(named("real_repo")) {
        GitHubRepository(get())
    }
    single<SchedulerProvider>(named("scheduler_provider")) { SearchSchedulerProvider() }
    viewModel {
        SearchViewModel(get(named("real_repo")),
            get(named("scheduler_provider")))
    }
}