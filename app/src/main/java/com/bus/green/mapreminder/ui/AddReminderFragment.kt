package com.bus.green.mapreminder.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bus.green.mapreminder.R
import com.bus.green.mapreminder.common.addMapStyle
import com.bus.green.mapreminder.common.setCameraPosition
import com.bus.green.mapreminder.location.FusedLocationProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng


private const val ADD_REMINDER_TAG = "AddReminderFragment"

class AddReminderFragment : Fragment(), OnMapReadyCallback {


    private var map: GoogleMap? = null

    private lateinit var locationProvider: FusedLocationProvider
    private lateinit var latLng: LatLng

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        locationProvider = FusedLocationProvider(context!!)

        locationProvider.requestUpdate { latitude, longitude ->
            Log.d("location", "latitude $latitude, longitude $longitude")
            latLng = LatLng(latitude, longitude)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.fragment_add_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = this.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        with(map) {
            addMapStyle(context!!)
            this?.uiSettings?.isMyLocationButtonEnabled = false
            this?.uiSettings?.isMapToolbarEnabled = false
            this?.isMyLocationEnabled = true
        }

        map?.setCameraPosition(latLng)
    }


    override fun onPause() {
        super.onPause()
        locationProvider.cancelRequest { _, _ -> }
    }
}