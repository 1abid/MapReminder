package com.bus.green.mapreminder.location

interface LocationProvider {

    fun requestUpdate(callback: (latitude:Double, longitude: Double) -> Unit)

    fun cancelRequest(callback: (latitude: Double, longitude: Double) -> Unit)
}