package com.bus.green.mapreminder

import android.app.Application
import com.bus.green.mapreminder.common.lazyFast
import com.bus.green.mapreminder.reminder.ReminderRepository

class ReminderApplication: Application() {

    private lateinit var repository: ReminderRepository

    override fun onCreate() {
        super.onCreate()
        repository = ReminderRepository(this)
    }

    fun getRepository() = repository
}