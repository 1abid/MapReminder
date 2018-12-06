package com.bus.green.mapreminder.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.bus.green.mapreminder.R
import com.bus.green.mapreminder.common.addMapStyle
import com.bus.green.mapreminder.common.animateCamera
import com.bus.green.mapreminder.common.hideKeyboard
import com.bus.green.mapreminder.common.setCameraPosition
import com.bus.green.mapreminder.location.LocationProvider
import com.bus.green.mapreminder.model.CurrentLocation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_add_reminder.*
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject
import javax.inject.Named


private const val ADD_REMINDER_TAG = "AddReminderFragment"

class AddReminderFragment : Fragment(), OnMapReadyCallback {


    private var map: GoogleMap? = null

    @Inject
    @field:Named("addFragment")
    lateinit var currentLocation: LocationProvider
    private lateinit var latLng: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_reminder, container, false)

        val mapFragment = this.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_place.addTextChangedListener(placeTextWatcher)

        search_iv.setOnCheckedChangeListener { buttonView, isChecked ->
            if(!isChecked){
                search_place.setText("", TextView.BufferType.EDITABLE)
                context?.hideKeyboard(buttonView)
                if(map_marker.visibility == View.GONE)
                    map_marker.markerVisibility()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        currentLocation.requestUpdate(::getLocation)
    }

    private fun getLocation(currentLocation: CurrentLocation){
        latLng = currentLocation.latLng
        map?.setCameraPosition(latLng)
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
        user_location?.setOnClickListener {
            map?.animateCamera(latLng)
        }
    }


    override fun onPause() {
        super.onPause()
        currentLocation.cancelRequest(::getLocation)
    }

    private val placeTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(count > 0){
                search_iv?.apply { if(!this.isChecked) this.isChecked = true }
                map_marker.markerVisibility(View.GONE)
            }
        }

        override fun afterTextChanged(s: Editable?) {
            if(s?.length!! == 0){
                search_iv?.apply { if(this.isChecked) this.isChecked = false }
                map_marker.markerVisibility()
            }
        }

    }

    private fun LottieAnimationView.markerVisibility(visibility: Int = View.VISIBLE) { this.visibility = visibility }
}

