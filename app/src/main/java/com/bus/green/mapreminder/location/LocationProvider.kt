package com.bus.green.mapreminder.location

import com.bus.green.mapreminder.model.CurrentLocation

interface LocationProvider {

    fun requestUpdate(callback: (currentLocation: CurrentLocation) -> Unit)

    fun cancelRequest(callback: (currentLocation: CurrentLocation) -> Unit)
}