package com.bus.green.mapreminder.model


import androidx.lifecycle.LiveData
import com.bus.green.mapreminder.location.LocationProvider
import javax.inject.Inject

class CurrentLocationLiveData @Inject constructor(
    private val locationProvider: LocationProvider
): LiveData<CurrentLocation>() {

    override fun onActive() {
        super.onActive()
        locationProvider.requestUpdate(::requestLocation)
    }

    private fun requestLocation(currentLocation: CurrentLocation){
        postValue(currentLocation)
    }

    override fun onInactive() {
        locationProvider.cancelRequest(::requestLocation)
        super.onInactive()
    }

}