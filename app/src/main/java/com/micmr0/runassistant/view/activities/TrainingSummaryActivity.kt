package com.micmr0.runassistant.view.activities

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.micmr0.runassistant.Constants
import com.micmr0.runassistant.R
import com.micmr0.runassistant.db.DatabaseHelper
import com.micmr0.runassistant.db.TrainingDao
import kotlinx.android.synthetic.main.activity_training_summary.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.micmr0.runassistant.util.LapsAdapter
import java.util.concurrent.TimeUnit

class TrainingSummaryActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var mMapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private lateinit var mRoute: Polyline

    private var mTrainingId = -1L
    private lateinit var mTrainingInfo: TrainingDao
    private lateinit var mTrainingRoute: List<LatLng>
    private lateinit var mDatabaseHelper: DatabaseHelper

    private lateinit var mTrainingSummaryNameTextView: TextView
    private lateinit var mTrainingSummaryLocationTextView: TextView
    private lateinit var mTrainingSummaryTimeTextView: TextView
    private lateinit var mTrainingSummaryDistanceTextView: TextView
    private lateinit var mTrainingSummaryCaloriesTextView: TextView
    private lateinit var mTrainingSummaryAverageSpeedTextView: TextView
    private lateinit var mTrainingSummaryMaxSpeedTextView: TextView
    private lateinit var mTrainingSummaryMinAltitudeTextView: TextView
    private lateinit var mTrainingSummaryMaxAltitudeTextView: TextView
    private lateinit var mTrainingSummaryWeatherLabelTextView: TextView
    private lateinit var mTrainingWeatherImageView: ImageView
    private lateinit var mTrainingWeatherTempTextView: TextView
    private lateinit var mTrainingWeatherPressureTextView: TextView
    private lateinit var mTrainingWeatherHumidityTextView: TextView
    private lateinit var mTrainingWeatherWindSpeedTextView: TextView

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: LapsAdapter

    override fun getLayoutResId(): Int {
        return R.layout.activity_training_summary
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMapFragment = training_summary_map as SupportMapFragment

        mRecyclerView = activity_training_summary_laps_recycler_view as RecyclerView

        mTrainingSummaryNameTextView = activity_training_summary_name_text_view
        mTrainingSummaryLocationTextView = activity_training_summary_location_text_view
        mTrainingSummaryTimeTextView = activity_training_summary_time_text_view
        mTrainingSummaryDistanceTextView = activity_training_summary_distance_text_view
        mTrainingSummaryCaloriesTextView = activity_training_summary_calories_text_view
        mTrainingSummaryAverageSpeedTextView = activity_training_summary_average_speed_text_view
        mTrainingSummaryMaxSpeedTextView = activity_training_summary_max_speed_text_view
        mTrainingSummaryMinAltitudeTextView = activity_training_summary_min_altitude_text_view
        mTrainingSummaryMaxAltitudeTextView = activity_training_summary_max_altitude_text_view
        mTrainingSummaryWeatherLabelTextView = activity_training_summary_weather_label
        mTrainingWeatherTempTextView = activity_training_summary_weather_text_view
        mTrainingWeatherPressureTextView = activity_training_summary_weather_pressure_text_view
        mTrainingWeatherHumidityTextView = activity_training_summary_weather_humidity_text_view
        mTrainingWeatherWindSpeedTextView = activity_training_summary_weather_wind_speed_text_view

        mTrainingWeatherImageView = activity_training_summary_weather_icon

        mDatabaseHelper = DatabaseHelper(this)

        if (intent.hasExtra(Constants.IntentExtras.TRAINING_ID)) {
            mTrainingId = intent.getLongExtra(Constants.IntentExtras.TRAINING_ID, -1)

            mTrainingInfo = mDatabaseHelper.getTrainingInfo(mTrainingId)
            mTrainingRoute = mDatabaseHelper.getTrainingRoute(mTrainingId)

            mAdapter = LapsAdapter(this, mTrainingInfo.mLaps!!)
            val mLayoutManager = LinearLayoutManager(this)

            mRecyclerView.layoutManager = mLayoutManager
            mRecyclerView.itemAnimator = DefaultItemAnimator()
            mRecyclerView.adapter = mAdapter
        }

        if (onRequestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, BaseActivity.ACCESS_FINE_LOCATION)) {
            mMapFragment.getMapAsync(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_training_summary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_delete_training) {

            val alertDialog = AlertDialog.Builder(
                    this)

            alertDialog.setPositiveButton(getString(R.string.training_end_yes)) { _, _ ->
                val databaseHelper = DatabaseHelper(baseContext)
                databaseHelper.deleteTraining(mTrainingId)

                setResult(Constants.ResultCodes.DELETE_TRAINING)
                finish()
            }

            alertDialog.setNegativeButton(getString(R.string.training_end_cancel), null)
            alertDialog.setMessage(getString(R.string.delete_training_message))
            alertDialog.setTitle(getString(R.string.delete_plan_title))
            alertDialog.show()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(pMap: GoogleMap?) {
        mMap = pMap!!

        val polylineOptions = PolylineOptions()
        polylineOptions.color(Color.BLUE)
        polylineOptions.width(12f)

        mRoute = mMap.addPolyline(polylineOptions)

        mRoute.points = mTrainingRoute

        val zoomBuilder = LatLngBounds.Builder()
        for (latLng in mTrainingRoute) {
            zoomBuilder.include(latLng)
        }

        val latLngBounds = zoomBuilder.build()

        mMap.setOnMapLoadedCallback { mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10)) }

        mTrainingSummaryNameTextView.text = mTrainingInfo.mName
        mTrainingSummaryLocationTextView.text = mTrainingInfo.mLocation

        mTrainingSummaryTimeTextView.text = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mTrainingInfo.mTime.toLong()),
                TimeUnit.MILLISECONDS.toMinutes(mTrainingInfo.mTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(mTrainingInfo.mTime.toLong()) % 60)

        mTrainingSummaryDistanceTextView.text = getString(R.string.training_distance_value, Math.round(mTrainingInfo.mDistance) / 1000, Math.round(mTrainingInfo.mDistance) % 1000)

        mTrainingSummaryCaloriesTextView.text = mTrainingInfo.mCalories.toString()

        mTrainingSummaryAverageSpeedTextView.text = getString(R.string.training_speed_value, mTrainingInfo.mAverageSpeed)
        mTrainingSummaryMaxSpeedTextView.text = getString(R.string.training_speed_value, mTrainingInfo.mMaxSpeed)

        mTrainingSummaryMinAltitudeTextView.text = mTrainingInfo.mMinAltitude.toString()
        mTrainingSummaryMaxAltitudeTextView.text = mTrainingInfo.mMaxAltitude.toString()

        mTrainingSummaryWeatherLabelTextView.text = String.format(getString(R.string.activity_training_summary_weather_label), mTrainingInfo.mWeatherDescription)

        Glide.with(this).load(getString(R.string.weather_api_link_image, mTrainingInfo.mWeatherIcon)).into(mTrainingWeatherImageView)

        mTrainingWeatherTempTextView.text = getString(R.string.activity_training_summary_temperature, (mTrainingInfo.mWeatherTemperature).toInt())

        checkTemperatureColor()

        mTrainingWeatherHumidityTextView.text = getString(R.string.activity_training_summary_humidity, mTrainingInfo.mWeatherHumidity)
        mTrainingWeatherPressureTextView.text = getString(R.string.activity_training_summary_pressure, mTrainingInfo.mWeatherPressure)
        mTrainingWeatherWindSpeedTextView.text = getString(R.string.activity_training_summary_wind_speed, mTrainingInfo.mWeatherWindSpeed * 3.6) // to km/h
    }

    private fun checkTemperatureColor() {

        when (mTrainingInfo.mWeatherTemperature.toInt()) {
            in -50..5 -> mTrainingWeatherTempTextView.setTextColor(ContextCompat.getColor(applicationContext, R.color.cold))

            in 6..15 -> mTrainingWeatherTempTextView.setTextColor(ContextCompat.getColor(applicationContext, R.color.medium))

            in 16..50 -> mTrainingWeatherTempTextView.setTextColor(ContextCompat.getColor(applicationContext, R.color.hot))
        }
    }


    override fun onPermissionGranted(pPermission: String) {
        // nothing to do
    }
}