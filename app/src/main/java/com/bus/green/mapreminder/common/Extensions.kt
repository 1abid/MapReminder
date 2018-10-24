package com.bus.green.mapreminder.common

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.checkPermession(vararg permissions: String, funtion:() -> Unit){
    if(permissions.any { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_DENIED })
        funtion()
}

fun Context.isGrantedPermission(vararg permissions: String , function: () -> Unit){
    if(permissions.all { ContextCompat.checkSelfPermission(this , it) == PackageManager.PERMISSION_GRANTED }){
        function()
    }
}

fun <T> lazyFast(operation: () -> T):Lazy<T> = lazy(LazyThreadSafetyMode.NONE){
    operation()
}