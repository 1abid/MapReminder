package com.bus.green.mapreminder.di

import com.bus.green.mapreminder.ui.AddReminderFragment
import com.bus.green.mapreminder.ui.mainFragment.FragmentMain
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidBuilder {

    @ContributesAndroidInjector
    abstract fun bindMainFragment(): FragmentMain

    @ContributesAndroidInjector
    abstract fun bindAddRemimderFragment(): AddReminderFragment
}