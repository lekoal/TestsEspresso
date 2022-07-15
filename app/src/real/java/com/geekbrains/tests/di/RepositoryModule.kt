package com.geekbrains.tests.di

import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.repository.GitHubApi
import com.geekbrains.tests.repository.GitHubRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val repoModule = module {
    single(named("api_url")) { "https://api.github.com" }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(get<String>(named("api_url")))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<GitHubApi> {
        get<Retrofit>().create(GitHubApi::class.java)
    }
    single<RepositoryContract>(named("real_repo")) {
        GitHubRepository(get())
    }
}