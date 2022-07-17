package com.geekbrains.tests.utils

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun ui(): Scheduler
    fun background(): Scheduler
}