package com.bus.green.mapreminder.reminder

import com.google.android.gms.maps.model.LatLng
import java.util.*

class Reminder(val id:String = UUID.randomUUID().toString(),
               var latLng: LatLng?,
               var radius: Double?,
               var message: String?) {

}