package com.bus.green.mapreminder.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.location.component1
import androidx.core.location.component2
import com.bus.green.mapreminder.common.isGrantedPermission
import com.bus.green.mapreminder.common.lazySafe
import com.bus.green.mapreminder.model.CurrentLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

const val TAG = "FUSED_LOCATION_PROVIDER"
class FusedLocationProvider(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationProvider {


    private val permissions: Array<out String> =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    private val subscribers by lazySafe {
        mutableListOf<(currentLocation: CurrentLocation) -> Unit>()
    }


    @SuppressLint("MissingPermission")
    override fun requestUpdate(callback: (currentLocation:CurrentLocation) -> Unit) {

        if (subscribers.isEmpty()) {
            subscribers.add(callback)
            context.isGrantedPermission(permissions) {
                LocationRequest.create().also { locationRequest ->
                    locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER

                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
                }
            }

        } else
            subscribers.add(callback)
    }

    override fun cancelRequest(callback: (currentLocation:CurrentLocation) -> Unit) {

        subscribers.remove(callback)
        if(subscribers.isEmpty())
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    private val locationCallback = object : LocationCallback() {

        override fun onLocationAvailability(availability: LocationAvailability?) {
            super.onLocationAvailability(availability)
            Log.d(TAG, "location availability ${availability?.isLocationAvailable}")
        }

        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.let {result ->
                result.locations.firstOrNull()?.also {location ->
                    val (latitude, longitude) = location
                    subscribers.forEach {callback->
                        callback(CurrentLocation(latitude, longitude))
                    }
                }
            }
        }
    }


}