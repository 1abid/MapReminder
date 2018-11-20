package com.bus.green.mapreminder

import android.app.Application
import androidx.fragment.app.Fragment
import com.bus.green.mapreminder.di.DaggerMapReminderComponent
import com.bus.green.mapreminder.di.MapReminderComponent
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class ReminderApplication: Application(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentAndroidDispatcher: DispatchingAndroidInjector<Fragment>

    private val mapReminderComponent: MapReminderComponent by lazy {
        DaggerMapReminderComponent.builder().application(this).build()
    }


    override fun onCreate() {
        super.onCreate()

        mapReminderComponent.inject(this)
        AndroidThreeTen.init(this)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentAndroidDispatcher

}