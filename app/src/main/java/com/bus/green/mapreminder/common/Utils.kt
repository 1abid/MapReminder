package com.bus.green.mapreminder.common

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.bus.green.mapreminder.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions


fun GoogleMap?.addMapStyle(context: Context){
    try {
        // Customise the styling of the base map using a JSON object defined
        // in a raw resource file.
        val success = this?.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.styled_map))

        if (!success!!) {
            Log.e("STYLING MAP", "Style parsing failed.")
        }
    } catch (e: Resources.NotFoundException) {
        Log.e("STYLING MAP", "Can't find style. Error: ", e)
    }
}