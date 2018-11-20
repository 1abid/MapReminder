package com.bus.green.mapreminder.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class MapReminderModule {

    @Provides
    @Singleton
    fun providesContext(application: Application): Context = application
}