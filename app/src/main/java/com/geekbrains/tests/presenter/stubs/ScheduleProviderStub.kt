package com.geekbrains.tests.presenter.stubs

import com.geekbrains.tests.utils.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class ScheduleProviderStub : SchedulerProvider {
    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun background(): Scheduler {
        return Schedulers.trampoline()
    }
}