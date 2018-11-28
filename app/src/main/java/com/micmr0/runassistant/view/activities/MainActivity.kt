package com.micmr0.runassistant.view.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.micmr0.runassistant.R
import com.micmr0.runassistant.util.Logger
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.micmr0.runassistant.Constants
import com.micmr0.runassistant.util.GooglePlayServicesHelper
import com.micmr0.runassistant.view.OnFragmentInteractionListener
import com.micmr0.runassistant.view.fragments.*


class MainActivity : BaseActivity(), OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener {
    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var mCurrentFragment: Fragment

    private var mHistoryFragment: Fragment? = null
    private var mPlansFragment: Fragment? = null
    private lateinit var mMapFragment: Fragment
    private var mAchievementsFragment: Fragment? = null
    private var mAppInfoFragment: Fragment? = null
    private var mSettings: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(this, "onCreate")

        hideTitle()

        mBottomNavigationView = findViewById(R.id.bottom_navigation_view)

        if (intent.extras != null) {
            mMapFragment = MapFragment.newInstance(intent.extras.getParcelable(Constants.IntentExtras.LOCATION), intent.extras.getString(Constants.IntentExtras.LAST_LOCATION_NAME), intent.extras.getString(Constants.IntentExtras.WEATHER_LOCATION_NAME))
        } else {
            mMapFragment = MapFragment()
        }

        supportFragmentManager.beginTransaction().replace(R.id.activity_main_container, mMapFragment).addToBackStack(null).commit()

        mBottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.history -> {
                    if (mHistoryFragment == null) {
                        mCurrentFragment = HistoryFragment()
                        mHistoryFragment = mCurrentFragment
                    } else {
                        mCurrentFragment = mHistoryFragment as Fragment
                    }
                }

                R.id.plans -> {
                    if (mPlansFragment == null) {
                        mCurrentFragment = PlansFragment()
                        mPlansFragment = mCurrentFragment
                    } else {
                        mCurrentFragment = mPlansFragment as Fragment
                    }
                }

                R.id.run -> {
                    mCurrentFragment = mMapFragment
                }

                R.id.achievments -> {
                    if (mAchievementsFragment == null) {
                        mCurrentFragment = AchievementsFragment()
                        mAchievementsFragment = mCurrentFragment
                    } else {
                        mCurrentFragment = mAchievementsFragment as Fragment
                    }

                }

                R.id.appInfo -> {
                    if (mAppInfoFragment == null) {
                        mCurrentFragment = AppInfoFragment()
                        mAppInfoFragment = mCurrentFragment
                    } else {
                        mCurrentFragment = mAppInfoFragment as Fragment
                    }

                }
            }

            replaceFragment(mCurrentFragment)

            true
        }

        mBottomNavigationView.selectedItemId = R.id.run

        checkInternetConnection()

        if (!GooglePlayServicesHelper.checkGooglePlayServices(this)) {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (R.id.action_settings == item!!.itemId) {
            if (mSettings == null) {
                mCurrentFragment = Settings()
                mSettings = mCurrentFragment
            } else {
                mCurrentFragment = mSettings as Fragment
            }

            replaceFragment(mCurrentFragment)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onFragmentInteraction(uri: Uri) {
        if (uri == Uri.parse(Constants.Uri.INITIALIZE_TRAINING) || uri == Uri.parse(Constants.Uri.DELETE_TRAINING)) {
            mBottomNavigationView.selectedItemId = R.id.run
        }
    }

    override fun onRequestPermission(pPermission: String, pRequestCode: Int): Boolean {
        return onRequestPermission(this, pPermission, pRequestCode)
    }

    override fun changeBottomNavigationVisibility(pVisibility: Int) {
        mBottomNavigationView.visibility = pVisibility
    }

    override fun onPermissionGranted(pPermission: String) {
        when (pPermission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                mBottomNavigationView.selectedItemId = R.id.run
                (mMapFragment as MapFragment).getMapAsync()
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (mCurrentFragment is AppInfoFragment && count > 1) {
            supportFragmentManager.popBackStackImmediate()
            replaceFragment(mAppInfoFragment as Fragment)
        } else {
            super.onBackPressed()
            super.onBackPressed()
        }
    }

    private fun replaceFragment(pFragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.activity_main_container, pFragment).commit()
    }

    private fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true))
                if (ni.isConnected)
                    haveConnectedWifi = true
            if (ni.typeName.equals("MOBILE", ignoreCase = true))
                if (ni.isConnected)
                    haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }

    private fun checkInternetConnection() {
        if (!haveNetworkConnection()) {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialog.setMessage(getString(R.string.activity_main_need_network_info))
            alertDialog.setNeutralButton(getString(R.string.ok)) { _: DialogInterface, _: Int ->
                run {
                    startActivityForResult(Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0)
                }
            }
            alertDialog.show()
        }
    }
}
