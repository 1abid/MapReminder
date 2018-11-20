package com.bus.green.mapreminder.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.bus.green.mapreminder.R
import com.bus.green.mapreminder.ReminderApplication
import com.bus.green.mapreminder.common.*
import com.bus.green.mapreminder.reminder.ReminderRepository

class MainActivity : AppCompatActivity() {

    private val requestCode = 0
    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var requiresPermission = false

    private val host: NavHostFragment? by lazyFast {
        supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment?
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setupActionBarWithNavController(host!!.navController)

        checkPermission(*permissions){
            requestPermission()
        }
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
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.any { PackageManager.PERMISSION_DENIED == it }) {
            requiresPermission = true
        } else {
            requiresPermission = false
            findNavController(R.id.my_nav_host_fragment).navigate(R.id.fragmentMain)
        }
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.my_nav_host_fragment).navigateUp()

}