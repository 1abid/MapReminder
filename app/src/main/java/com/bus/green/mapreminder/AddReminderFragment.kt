package com.bus.green.mapreminder

import android.annotation.SuppressLint
import android.app.Service
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bus.green.mapreminder.common.addMapStyle
import com.bus.green.mapreminder.common.lazyFast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class AddReminderFragment: Fragment(), OnMapReadyCallback {


    private var map: GoogleMap? = null

    private val locationManager: LocationManager by lazyFast {
        activity?.getSystemService(Service.LOCATION_SERVICE) as LocationManager
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
        with(map){
            addMapStyle(context!!)
            this?.uiSettings?.isMyLocationButtonEnabled   = false
            this?.uiSettings?.isMapToolbarEnabled = false
            this?.isMyLocationEnabled = true
        }
    }
}