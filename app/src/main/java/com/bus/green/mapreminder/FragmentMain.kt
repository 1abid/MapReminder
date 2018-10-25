package com.bus.green.mapreminder

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.content.res.Resources.NotFoundException
import androidx.navigation.fragment.NavHostFragment
import com.bus.green.mapreminder.common.isGrantedPermission
import com.bus.green.mapreminder.common.lazyFast
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.android.synthetic.main.fragment_main.*


class FragmentMain : Fragment(), OnMapReadyCallback {

    private var map: GoogleMap? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mapFragment = this.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }



    @SuppressLint("RestrictedApi")
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
        setMapStyle(map)
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