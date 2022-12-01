package org.wit.valueGuitar.helpers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import java.util.concurrent.TimeUnit

val REQUEST_PERMISSIONS_REQUEST_CODE = 34

fun checkLocationPermissions(activity: Activity) : Boolean {
    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        return true
    }
    else {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
        return false
    }
}
@SuppressLint("RestrictedApi")
fun createDefaultLocationRequest() : LocationRequest {
    val locationRequest = LocationRequest.create().apply{
        interval = TimeUnit.SECONDS.toMillis(60)
        fastestInterval = TimeUnit.SECONDS.toMillis(30)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    return locationRequest
}