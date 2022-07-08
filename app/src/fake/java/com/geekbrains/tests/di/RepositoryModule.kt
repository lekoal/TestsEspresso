package com.geekbrains.tests.di

import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.repository.FakeGitHubRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repoModule = module {
    single<RepositoryContract>(named("fake_repo")) {
        FakeGitHubRepository()
    }
}
