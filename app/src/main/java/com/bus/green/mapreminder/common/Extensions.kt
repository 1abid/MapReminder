package com.bus.green.mapreminder.common

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.checkPermission(vararg permissions: String, function:() -> Unit){
    if(permissions.any { this.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED })
        function()
}

fun Context.isGrantedPermission(vararg permissions: String , function: () -> Unit){
    if(permissions.all { this.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }){
        function()
    }
}

fun Context.isDeniedPermission(vararg permissions: String , function: () -> Unit){
    if(permissions.any { this.checkSelfPermission(it) == PackageManager.PERMISSION_DENIED }){
        function()
    }
}

fun <T> lazyFast(operation: () -> T):Lazy<T> = lazy(LazyThreadSafetyMode.NONE){
    operation()
}

fun <T> lazySafe(operation: () -> T):Lazy<T> = lazy(LazyThreadSafetyMode.SYNCHRONIZED){
    operation()
}