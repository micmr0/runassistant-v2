package com.micmr0.runassistant.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.micmr0.runassistant.R
import com.micmr0.runassistant.util.Logger
import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager

abstract class BaseActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {
        var ACCESS_FINE_LOCATION = 1
    }

    private var actionBar : ActionBar? = null
    lateinit var mToolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.d(this, "onCreate")

        setContentView(getLayoutResId())

        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        initStatusBar(mToolbar)

        actionBar = supportActionBar

        setStatusBarTransparent()
    }

    protected abstract fun getLayoutResId() : Int

    protected fun hideTitle() {
        actionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESS_FINE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                finish()
            }
        }
    }


    protected fun onRequestPermission(pActivity : Activity, pPermission : String, pRequestCode : Int) : Boolean {
        if (ActivityCompat.checkSelfPermission(pActivity, pPermission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(pActivity, pPermission)) {
                val alertDialog = AlertDialog.Builder(pActivity)
                alertDialog.setMessage(getString(R.string.fragment_map_location_permission_info))
                alertDialog.setNeutralButton(getString(R.string.ok), { _, _ ->
                    ActivityCompat.requestPermissions( pActivity, Array(1, { pPermission}), pRequestCode)
                })
                alertDialog.show()
            } else {
                ActivityCompat.requestPermissions( pActivity, Array(1, { pPermission}), pRequestCode)
            }
            return false

        }
        return true
    }

    abstract fun onPermissionGranted(pPermission: String)

    private fun initStatusBar(toolbar: View) {
        if (Build.VERSION.SDK_INT >= 19) {
            val contentParent = findViewById<View>(android.R.id.content) as ViewGroup
            val content = contentParent.getChildAt(0)

            setFitsSystemWindows(content, false, true)

            clipToStatusBar(toolbar)
        }
    }

    private fun setFitsSystemWindows(view: View?, fitSystemWindows: Boolean, applyToChildren: Boolean) {
        if (view == null) return
        view.fitsSystemWindows = fitSystemWindows
        if (applyToChildren && view is ViewGroup) {
            val viewGroup = view as ViewGroup?
            var i = 0
            val n = viewGroup!!.childCount
            while (i < n) {
                viewGroup.getChildAt(i).fitsSystemWindows = fitSystemWindows
                i++
            }
        }
    }

    private  fun clipToStatusBar(view: View) {
        val statusBarHeight = getStatusBarHeight()
        view.layoutParams.height += statusBarHeight
        view.setPadding(0, statusBarHeight, 0, 0)
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}