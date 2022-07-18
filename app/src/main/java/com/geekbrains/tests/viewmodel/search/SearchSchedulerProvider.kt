package com.geekbrains.tests.viewmodel.search

import com.geekbrains.tests.utils.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchSchedulerProvider: SchedulerProvider {
    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun background(): Scheduler {
        return Schedulers.io()
    }
}