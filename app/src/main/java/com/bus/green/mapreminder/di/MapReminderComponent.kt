package com.bus.green.mapreminder.di

import android.app.Application
import com.bus.green.mapreminder.ReminderApplication
import com.bus.green.mapreminder.location.LocationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        AndroidBuilder::class,
        MapReminderModule::class,
        LocationModule::class,
        ViewModelModule::class]
)
interface MapReminderComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): MapReminderComponent
    }

    fun inject(application: ReminderApplication)
}