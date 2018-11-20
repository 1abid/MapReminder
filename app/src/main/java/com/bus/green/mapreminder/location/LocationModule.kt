package com.bus.green.mapreminder.location

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class LocationModule {

    @Provides
    @Singleton
    fun providesFusedLocationClient(context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    @Named("mainFragment")
    fun providesFusedLocationProviderMain(context: Context, fusedLocationProviderClient: FusedLocationProviderClient): LocationProvider =
            FusedLocationProvider(context, fusedLocationProviderClient)

    @Provides
    @Singleton
    @Named("addFragment")
    fun providesFusedLocationProviderAddReminder(context: Context, fusedLocationProviderClient: FusedLocationProviderClient): LocationProvider =
        FusedLocationProvider(context, fusedLocationProviderClient)
}