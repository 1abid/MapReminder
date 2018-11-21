package com.bus.green.mapreminder.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bus.green.mapreminder.R
import com.bus.green.mapreminder.common.addMapStyle
import com.bus.green.mapreminder.common.animateCamera
import com.bus.green.mapreminder.common.isDeniedPermission
import com.bus.green.mapreminder.common.isGrantedPermission
import com.bus.green.mapreminder.location.LocationProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject
import javax.inject.Named

const val TAG_FRAGMENT_MAIN = "MAIN FRAGMENT"

class FragmentMain : Fragment(), OnMapReadyCallback {

    private val permissions =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    companion object {
        const val mapZoom = 15f
    }

    private var map: GoogleMap? = null

    @Inject
    @field:Named("mainFragment")
    lateinit var locationProvider: LocationProvider
    private lateinit var latLng: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = this.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        newReminder?.setOnClickListener {
            findNavController().navigate(R.id.add_reminder_action)
        }

    }


    @SuppressLint("RestrictedApi", "MissingPermission")
    override fun onResume() {
        super.onResume()

        context?.isGrantedPermission(*permissions) {
            newReminder.visibility = View.VISIBLE
            currentLocation.visibility = View.VISIBLE

            locationProvider.requestUpdate(::bind)
        }

        context?.isDeniedPermission(*permissions){
            newReminder.visibility = View.GONE
            currentLocation.visibility = View.GONE

            val options = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build()
            findNavController().navigate(R.id.missingFragment, null, options)

        }


    }


    private fun bind(latitude: Double, longitude: Double) {
        latLng = LatLng(latitude, longitude)
        map.animateCamera(latLng)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        with(this.map) {
            addMapStyle(context!!)
            this?.uiSettings?.isMyLocationButtonEnabled = false
            this?.uiSettings?.isMapToolbarEnabled = false
            context?.isGrantedPermission(*permissions) {
                this?.isMyLocationEnabled = true
            }
        }
        onMapAndPermissionReady()
    }

    @SuppressLint("MissingPermission")
    private fun onMapAndPermissionReady() {
        map?.let {
            currentLocation?.setOnClickListener {
                locationProvider.requestUpdate(::bind)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        locationProvider.cancelRequest(::bind)
    }

}