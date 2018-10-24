package com.bus.green.mapreminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.bus.green.mapreminder.common.isGrantedPermission
import com.bus.green.mapreminder.common.lazyFast

class MainActivity : AppCompatActivity() {

    private val requestCode = 0
    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private var requiresPermission = false

    private val host: NavHostFragment? by lazyFast {
        supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setupActionBarWithNavController(host!!.navController)

        requestPermission()
    }


    override fun onResume() {
        super.onResume()



        isGrantedPermission(*permissions) {
            if (requiresPermission) {
                requiresPermission = false
            }

            findNavController(R.id.my_nav_host_fragment).navigate(R.id.fragmentMain)

        }
    }

    private fun requestPermission() {
        requiresPermission = true
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.any { PackageManager.PERMISSION_DENIED == it }) {

            val options = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build()
            findNavController(R.id.my_nav_host_fragment).navigate(R.id.missingFragment, null, options)

        } else {
            requiresPermission = false
        }
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.my_nav_host_fragment).navigateUp()
}