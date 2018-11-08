package com.bus.green.mapreminder.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.location.component1
import androidx.core.location.component2
import com.bus.green.mapreminder.common.isGrantedPermission
import com.bus.green.mapreminder.common.lazySafe
import com.google.android.gms.location.*

const val TAG = "FUSED_LOCATION_PROVIDER"
class FusedLocationProvider(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient = FusedLocationProviderClient(context)
) : LocationProvider {


    private val permissions =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    private val subscribers by lazySafe {
        mutableListOf<(latitude: Double, longitude: Double) -> Unit>()
    }


    @SuppressLint("MissingPermission")
    override fun requestUpdate(callback: (latitude: Double, longitude: Double) -> Unit) {
        if (subscribers.isEmpty()) {
            subscribers.add(callback)
            context.isGrantedPermission(*permissions) {
                LocationRequest.create().also { locationRequest ->
                    locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER

                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
                }
            }

        } else
            subscribers.add(callback)
    }

    override fun cancelRequest(callback: (latitude: Double, longitude: Double) -> Unit) {

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
            locationResult?.let {locationResult ->
                locationResult.locations.firstOrNull()?.also {location ->
                    val (latitude, longitude) = location
                    subscribers.forEach {callback->
                        callback(latitude, longitude)
                    }
                }
            }
        }
    }


}