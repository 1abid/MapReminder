package com.bus.green.mapreminder.common

import android.content.Context
import android.util.Log
import com.bus.green.mapreminder.R
import com.bus.green.mapreminder.ui.FragmentMain
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.lang.Exception
import java.util.*


fun GoogleMap?.addMapStyle(context: Context) {
    try {
        // Customise the styling of the base map using a JSON object defined
        // in a raw resource file.

        val success = if(shouldSetDarkMap(Instant.now())){
            this?.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.styled_map))
        }else
            this?.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.styled_map_day))

        if (!success!!) {
            Log.e("STYLING MAP", "Style parsing failed.")
        }
    } catch (e: Exception) {
        Log.e("STYLING MAP", "Can't find style. Error: ", e)
    }
}

fun GoogleMap?.animateCamera(latLng: LatLng, zoom: Float = FragmentMain.mapZoom) {
    this?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
}

fun GoogleMap?.setCameraPosition(latLng: LatLng, zoom: Float = FragmentMain.mapZoom) {
    CameraPosition.builder().target(latLng).zoom(zoom).build().also {
        this?.moveCamera(CameraUpdateFactory.newCameraPosition(it))
    }

}

fun shouldSetDarkMap(instant:Instant) :Boolean{
    val zoneId: ZoneId = ZoneId.systemDefault()
    val dateTimeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
        .withZone(zoneId)

    val time = LocalTime.parse("7:00 PM", dateTimeFormat)

    return !time.isAfter(LocalDateTime.ofInstant(instant, zoneId).toLocalTime())

}