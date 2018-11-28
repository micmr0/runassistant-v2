package com.micmr0.runassistant.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.location.Criteria
import android.location.LocationManager
import android.location.Location
import android.location.LocationListener


object SingleShotLocationProvider {

    interface LocationCallback {
        fun onNewLocationAvailable(location: Location)
    }
    @SuppressLint("MissingPermission")
    fun requestSingleUpdate(context: Context, callback: LocationCallback) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (isNetworkEnabled) {
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_COARSE
            locationManager.requestSingleUpdate(criteria, object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    callback.onNewLocationAvailable(location)
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }, null)
        } else {
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (isGPSEnabled) {
                val criteria = Criteria()
                criteria.accuracy = Criteria.ACCURACY_FINE
                locationManager.requestSingleUpdate(criteria, object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        callback.onNewLocationAvailable(location)
                    }

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }, null)
            }
        }
    }
}