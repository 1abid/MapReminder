package com.bus.green.mapreminder

import android.annotation.SuppressLint
import android.app.Service
import android.content.res.Resources
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bus.green.mapreminder.common.lazyFast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.android.synthetic.main.fragment_main.*

const val TAG = "MAIN FRAGMENT"


class FragmentMain : Fragment(), OnMapReadyCallback {

    private var map: GoogleMap? = null

    private val locationManager: LocationManager by lazyFast {
        activity?.getSystemService(Service.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mapFragment = this.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        }else{
            newReminder.visibility = View.VISIBLE
            currentLocation.visibility = View.VISIBLE
        }

    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        with(this.map){
            setMapStyle(this)
            this?.uiSettings?.isMyLocationButtonEnabled   = false
            this?.uiSettings?.isMapToolbarEnabled = false
        }
        onMapAndPermissionReady()
    }

    @SuppressLint("MissingPermission")
    private fun onMapAndPermissionReady() {
        if(map != null && !(activity as MainActivity).isRequiresPermission()){
            map?.isMyLocationEnabled = true
            currentLocation?.setOnClickListener {
                val bestProvider = locationManager.getBestProvider(Criteria(), true)
                val location = locationManager.getLastKnownLocation(bestProvider)
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }

        }
    }


    private fun setMapStyle(map: GoogleMap?){
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map?.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.styled_map))

            if (!success!!) {
                Log.e("STYLING MAP", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("STYLING MAP", "Can't find style. Error: ", e)
        }

    }
}