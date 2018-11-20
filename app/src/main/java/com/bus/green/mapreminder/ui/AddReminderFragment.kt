package com.bus.green.mapreminder.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bus.green.mapreminder.R
import com.bus.green.mapreminder.common.addMapStyle
import com.bus.green.mapreminder.common.hideKeyboard
import com.bus.green.mapreminder.common.setCameraPosition
import com.bus.green.mapreminder.location.FusedLocationProvider
import com.bus.green.mapreminder.location.LocationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_add_reminder.*
import javax.inject.Inject
import javax.inject.Named


private const val ADD_REMINDER_TAG = "AddReminderFragment"

class AddReminderFragment : Fragment(), OnMapReadyCallback {


    private var map: GoogleMap? = null

    @Inject @field:Named("addFragment") lateinit var currentLocation: LocationProvider
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
            }

        }
    }

    override fun onResume() {
        super.onResume()
        currentLocation.requestUpdate { latitude, longitude ->
            Log.d(ADD_REMINDER_TAG, "Current:latitude $latitude, Current:longitude $longitude")
            latLng = LatLng(latitude, longitude)
            Log.d(ADD_REMINDER_TAG, "latLng:latitude $latitude, latLng:longitude $longitude")
        }
        Log.d(ADD_REMINDER_TAG, "finishing onResume()")
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
    }


    override fun onPause() {
        super.onPause()
        currentLocation.cancelRequest { _, _ -> }
    }

    private val placeTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(count > 0){
                search_iv?.apply { if(!this.isChecked) this.isChecked = true }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            if(s?.length!! == 0){
                search_iv?.apply { if(this.isChecked) this.isChecked = false }
            }
        }

    }
}

