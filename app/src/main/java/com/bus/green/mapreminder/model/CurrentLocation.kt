package com.bus.green.mapreminder.model

import com.google.android.gms.maps.model.LatLng

data class CurrentLocation(val latitude: Double,
                           val longitude: Double,
                           val latLng: LatLng = LatLng(latitude, longitude))