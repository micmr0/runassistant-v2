package com.micmr0.runassistant.view.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.micmr0.runassistant.Constants
import com.micmr0.runassistant.R
import com.micmr0.runassistant.util.SharedPreferencesHelper
import com.micmr0.runassistant.util.SingleShotLocationProvider
import java.io.IOException
import java.util.*

class SplashActivity : AppCompatActivity() {
    private var mWeatherLocationName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            SingleShotLocationProvider.requestSingleUpdate(this, object : SingleShotLocationProvider.LocationCallback {
                override fun onNewLocationAvailable(location: Location) {
                    if (!SharedPreferencesHelper.isFirstRun()) {
                        val mainScreen = Intent(applicationContext, MainActivity::class.java)
                        mainScreen.putExtra(Constants.IntentExtras.LOCATION, location)
                        mainScreen.putExtra(Constants.IntentExtras.LAST_LOCATION_NAME, getAddressFromLocation(location))
                        mainScreen.putExtra(Constants.IntentExtras.WEATHER_LOCATION_NAME, mWeatherLocationName)
                        startActivity(mainScreen)
                    } else {
                        startActivity(Intent(applicationContext, IntroActivity::class.java))
                    }
                    finish()
                }
            })
        } else {
            startActivity(Intent(applicationContext, IntroActivity::class.java))
            finish()
        }
    }


    private fun getAddressFromLocation(pLocation : Location) : String {
        if(applicationContext != null) {
            val geocoder = Geocoder(applicationContext, Locale.getDefault())

            var addresses: List<Address> = emptyList()

            try {
                addresses = geocoder.getFromLocation(pLocation.latitude, pLocation.longitude, 1)
            } catch (ioException: IOException) {
            } catch (illegalArgumentException: IllegalArgumentException) { }

            // Handle case where no address was found.
            return if (addresses.isEmpty()) {
                applicationContext!!.getString(R.string.unknown_location)
            } else {
                val address = addresses[0]
                if(address.locality != null) {
                    mWeatherLocationName = address.locality
                }

                when {
                    address.thoroughfare != null && address.locality!= null -> address.thoroughfare + ", " + address.locality
                    address.thoroughfare != null -> address.thoroughfare
                    address.locality != null -> address.locality
                    else -> applicationContext!!.getString(R.string.unknown_location)
                }
            }
        }
        return ""
    }
}