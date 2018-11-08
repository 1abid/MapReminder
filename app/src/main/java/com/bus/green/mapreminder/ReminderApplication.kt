package com.bus.green.mapreminder

import android.app.Application
import com.bus.green.mapreminder.reminder.ReminderRepository
import com.jakewharton.threetenabp.AndroidThreeTen

class ReminderApplication: Application() {

    private lateinit var repository: ReminderRepository

    override fun onCreate() {
        super.onCreate()
        repository = ReminderRepository(this)

        AndroidThreeTen.init(this)
    }

    fun getRepository() = repository
}