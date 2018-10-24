package com.bus.green.mapreminder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

class FragmentMain:Fragment(), OnMapReadyCallback {

    private var map: GoogleMap? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        map?.let {
            Log.d("FragmentMain" , "map loaded")
        }
    }
}