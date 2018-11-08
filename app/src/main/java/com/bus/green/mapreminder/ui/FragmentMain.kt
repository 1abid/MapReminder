package com.bus.green.mapreminder.ui

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bus.green.mapreminder.R
import com.bus.green.mapreminder.common.addMapStyle
import com.bus.green.mapreminder.common.lazyFast
import com.bus.green.mapreminder.location.FusedLocationProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_main.*

const val TAG = "MAIN FRAGMENT"
const val LATITUDE_BUNDLE_KEY = "latitude"
const val LONGITUDE_BUNDLE_KEY = "longitude"
const val ZOOM_BUNDLE_KEY = "zoom"

class FragmentMain : Fragment(), OnMapReadyCallback {

    companion object {
        private const val mapZoom = 15f
    }

    private var map: GoogleMap? = null

    private lateinit var locationProvider: FusedLocationProvider
    private lateinit var latLng: LatLng

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        locationProvider = FusedLocationProvider(context!!)
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

        if ((activity as MainActivity).isRequiresPermission()) {
            newReminder.visibility = View.GONE
            currentLocation.visibility = View.GONE

            Handler().postDelayed({
                val options = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.slide_out_left)
                    .setPopEnterAnim(R.anim.slide_in_left)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build()
                findNavController().navigate(R.id.missingFragment, null, options)


            }, 3000)
        } else {
            newReminder.visibility = View.VISIBLE
            currentLocation.visibility = View.VISIBLE

            locationProvider.requestUpdate { latitude, longitude ->
                Log.d("location", "latitude $latitude, longitude $longitude")

                latLng = LatLng(latitude, longitude)
                cameraAnimation(latLng)
            }
        }

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        with(this.map) {
            addMapStyle(context!!)
            this?.uiSettings?.isMyLocationButtonEnabled = false
            this?.uiSettings?.isMapToolbarEnabled = false
            this?.isMyLocationEnabled = true
        }

        onMapAndPermissionReady()
    }

    @SuppressLint("MissingPermission")
    private fun onMapAndPermissionReady(){
        if((map != null && !(activity as MainActivity).isRequiresPermission())){
            currentLocation?.setOnClickListener {
                if (latLng == null)
                    Toast.makeText(context, "Location not found yet", Toast.LENGTH_SHORT)
                else
                    cameraAnimation(latLng)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        locationProvider.cancelRequest { _, _ ->  }
    }


    private fun cameraAnimation(latLng: LatLng, zoom: Float = mapZoom){
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mapZoom))
    }


}