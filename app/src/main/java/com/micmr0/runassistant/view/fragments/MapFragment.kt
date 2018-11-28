package com.micmr0.runassistant.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.content.res.AppCompatResources
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.micmr0.runassistant.R
import com.micmr0.runassistant.model.TrainingState
import com.micmr0.runassistant.model.pojo.TrainingData
import com.micmr0.runassistant.presenter.TrainingInfoPresenter
import com.micmr0.runassistant.util.Logger
import com.micmr0.runassistant.util.SingleShotLocationProvider
import com.micmr0.runassistant.view.IView
import kotlinx.android.synthetic.main.fragment_map.*
import java.util.*
import com.google.android.gms.maps.model.PolylineOptions
import com.micmr0.runassistant.Constants
import com.micmr0.runassistant.RAApplication
import com.micmr0.runassistant.util.SharedPreferencesHelper
import com.micmr0.runassistant.view.activities.BaseActivity
import java.io.IOException


class MapFragment : Fragment(), OnMapReadyCallback, IView<TrainingData> {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var mPresenter: TrainingInfoPresenter
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mGoogleMap: GoogleMap

    private lateinit var mGpsStatusDotImageView: ImageView

    private lateinit var mLastLocationName: String
    private var mWeatherLocationName = ""

    private lateinit var mTimeTextView: TextView
    private lateinit var mSpeedTextView: TextView
    private lateinit var mDistanceTextView: TextView
    private lateinit var mCaloriesTextView: TextView
    private lateinit var mMusicImageView : ImageView

    private lateinit var mStartTrainingButton : FloatingActionButton
    private lateinit var mStopTrainingButton: FloatingActionButton
    private lateinit var mActivityTypeTextView: TextView
    private lateinit var mTrainingTypeTextView: TextView

    private var mTrainingState = TrainingState.TRAINING_STOP

    private lateinit var mRoute: Polyline
    private lateinit var mTrainingSettingsContainer : LinearLayout

    private lateinit var mLastLocation: Location
    var mCheckedLastLocation = false

    companion object {
        fun newInstance(pLocation : Location, pLastLocationName: String, pWeatherLocationName : String) : MapFragment {
            val mapFragment = MapFragment()

            val args = Bundle()
            args.putParcelable(Constants.IntentExtras.LOCATION, pLocation)
            args.putString(Constants.IntentExtras.LAST_LOCATION_NAME, pLastLocationName)
            args.putString(Constants.IntentExtras.WEATHER_LOCATION_NAME, pWeatherLocationName)
            mapFragment.arguments = args

            return mapFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(this, "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = TrainingInfoPresenter()
        mPresenter.bind(this)

        mapFragment = childFragmentManager.findFragmentById((R.id.map)) as SupportMapFragment

        if(listener!!.onRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, BaseActivity.ACCESS_FINE_LOCATION)) {
            mapFragment.getMapAsync(this)
        }

        mStartTrainingButton = fragment_map_training_state_button
        mStartTrainingButton.setOnClickListener(handleTrainingState())

        mMusicImageView = fragment_map_music_image_view
        mMusicImageView.setOnClickListener { openMusicPlayer() }

        mGpsStatusDotImageView = gps_status_dot_image_view
        mTimeTextView = fragment_map_time_text_view
        mSpeedTextView = fragment_map_speed_text_view
        mDistanceTextView = fragment_map_distance_text_view
        mCaloriesTextView = fragment_map_calories_text_view

        mTrainingSettingsContainer = fragment_map_training_settings_container
        mActivityTypeTextView = fragment_map_activity_type_text_view
        mTrainingTypeTextView = fragment_map_training_type_text_view


        mActivityTypeTextView.setOnClickListener { _ ->
            run {
                val activities = arrayOf<CharSequence>(getString(R.string.activity_running), getString(R.string.activity_cycling))

                val alertDialog = AlertDialog.Builder(activity)
                alertDialog.setTitle(getString(R.string.choose_activity))
                alertDialog.setItems(activities) { _, i ->
                    run {
                        if (i == 0) {
                            SharedPreferencesHelper.setActivityType(0)
                            mActivityTypeTextView.setText(R.string.activity_running)
                        } else {
                            SharedPreferencesHelper.setActivityType(1)
                            mActivityTypeTextView.setText(R.string.activity_cycling)
                        }
                    }
                }
                alertDialog.show()
            }
        }

        if(RAApplication.sActivePlan != null) {
            mTrainingTypeTextView.text = RAApplication.sActivePlan!!.mActiveDayTitle
        }

        mStopTrainingButton = fragment_map_training_stop_button
        mStopTrainingButton.setOnClickListener { stopTraining() }


        mGpsStatusDotImageView.setColorFilter(Color.GREEN)

        if(mTrainingState == TrainingState.TRAINING_START) {
            fragment_map_training_state_button.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_pause))
            mTrainingSettingsContainer.visibility = View.GONE
        }

    }

    public fun getMapAsync() {
        mapFragment.getMapAsync(this)
    }


    public fun setTrainingType(pTrainingType : String) {
        mTrainingTypeTextView.text = pTrainingType
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(pGoogleMap: GoogleMap?) {
        Logger.d(this, "onMapReady")

        mGoogleMap = pGoogleMap!!
        mGoogleMap.isMyLocationEnabled = true
        mGoogleMap.setPadding(0, 600, 0, 110)

        if(arguments!= null && arguments!!.get(Constants.IntentExtras.LOCATION) != null) {
            mLastLocation = arguments!!.get(Constants.IntentExtras.LOCATION) as Location
            mLastLocationName = arguments!!.get(Constants.IntentExtras.LAST_LOCATION_NAME) as String
            mWeatherLocationName = arguments!!.get(Constants.IntentExtras.WEATHER_LOCATION_NAME) as String

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastLocation.latitude, mLastLocation.longitude), 16f))
        } else {
            SingleShotLocationProvider.requestSingleUpdate(requireContext(), object : SingleShotLocationProvider.LocationCallback {
                override fun onNewLocationAvailable(location: Location) {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 16f))
                    mLastLocation = location
                    mLastLocationName = getAddressFromLocation(location)
                }
            })
        }

        val polylineOptions = PolylineOptions()
        polylineOptions.color(Color.BLUE)
        polylineOptions.width(12f)

        mRoute = mGoogleMap.addPolyline(polylineOptions)

    }

    private fun getAddressFromLocation(pLocation : Location) : String {
        if(context != null) {
            val geocoder = Geocoder(context, Locale.getDefault())

            var addresses: List<Address> = emptyList()

            try {
                addresses = geocoder.getFromLocation(pLocation.latitude, pLocation.longitude, 1)
            } catch (ioException: IOException) {
            } catch (illegalArgumentException: IllegalArgumentException) { }

            // Handle case where no address was found.
            return if (addresses.isEmpty()) {
                context!!.getString(R.string.unknown_location)
            } else {
                val address = addresses[0]
                if(address.locality != null) {
                    mWeatherLocationName = address.locality
                }

                when {
                    address.thoroughfare != null && address.locality!= null -> address.thoroughfare + ", " + address.locality
                    address.thoroughfare != null -> address.thoroughfare
                    address.locality != null -> address.locality
                    else -> context!!.getString(R.string.unknown_location)
                }
            }
        }
        return ""
    }


    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun setLocationSource(pLocationSource: LocationSource) {
        mGoogleMap.setLocationSource(pLocationSource)
    }

    override fun onGetData(pData: TrainingData) {
        Logger.d(this, pData.mLocation.toString())

        val utc = TimeZone.getTimeZone("UTC")
        val cal = Calendar.getInstance(utc)
        cal.timeInMillis = pData.mElapsedTime

        val date = DateFormat.format("HH:mm:ss", cal).toString()
        mTimeTextView.text = date

        val speed = String.format("%.2f", pData.mLocation.speed * 3.6) // convert to km/h
        mSpeedTextView.text = speed

        val distance = String.format("%.2f", pData.mDistance / 1000, pData.mDistance % 1000)
        mDistanceTextView.text = distance

        mCaloriesTextView.text = pData.mCalories.toString()

        val location = LatLng(pData.mLocation.latitude, pData.mLocation.longitude)
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))

        val gpsSignal = pData.mLocation.accuracy.toInt()

        when {
            gpsSignal <= GpsSignal.GPS_GOOD.value -> {
                mGpsStatusDotImageView.setColorFilter(Color.GREEN)
            }
            gpsSignal <= GpsSignal.GPS_OK.value -> {
                mGpsStatusDotImageView.setColorFilter(Color.GREEN)
            }
            gpsSignal <= GpsSignal.GPS_WEAK.value -> {
                mGpsStatusDotImageView.setColorFilter(Color.YELLOW)
            }
            gpsSignal <= GpsSignal.GPS_BAD.value -> {
                mGpsStatusDotImageView.setColorFilter(Color.RED)
            }
        }

        drawRoute(pData.mLocation)
    }

    private fun handleTrainingState(): View.OnClickListener {
        return View.OnClickListener {
            when (mTrainingState) {
                TrainingState.TRAINING_STOP -> {
                    mapFragment.getMapAsync(this)

                    mTrainingState = TrainingState.TRAINING_START
                    (it as FloatingActionButton).setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_pause))
                    mPresenter.getLoader().startLogging(mLastLocationName, mWeatherLocationName)

                    mTrainingSettingsContainer.visibility = View.GONE
                }

                TrainingState.TRAINING_START, TrainingState.TRAINING_RESUME -> {
                    mTrainingState = TrainingState.TRAINING_PAUSE
                    (it as FloatingActionButton).setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_go))
                    mStopTrainingButton.visibility = View.VISIBLE
                    mPresenter.getLoader().pauseLogging()
                    mStopTrainingButton.visibility = View.VISIBLE
                }
                TrainingState.TRAINING_PAUSE -> {
                    mTrainingState = TrainingState.TRAINING_RESUME
                    (it as FloatingActionButton).setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_pause))
                    mStopTrainingButton.visibility = View.GONE
                    mPresenter.getLoader().resumeLogging(mLastLocationName, mWeatherLocationName)
                    mStopTrainingButton.visibility = View.GONE
                }
            }
        }
    }

    private fun drawRoute(pLocation: Location) {
        val points = mRoute.points
        points.add(LatLng(pLocation.latitude, pLocation.longitude))

        mRoute.points = points
    }

    private fun openMusicPlayer() {

        Logger.d(this, "Music openMusicPlayer")

        val intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_MUSIC)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    private fun stopTraining() {
        val alertDialog =  AlertDialog.Builder(context)

        alertDialog.setMessage(getString(R.string.fragment_map_stop_training_dialog))
        alertDialog.setPositiveButton(getString(R.string.training_end_yes)) { _, _ ->
            run {
                mPresenter.getLoader().stopLogging()
                mTrainingState = TrainingState.TRAINING_STOP
                mStopTrainingButton.visibility = View.GONE
                clearViews()

                mTrainingSettingsContainer.visibility = View.VISIBLE

                mGoogleMap.clear()
            }
        }
        alertDialog.setNegativeButton(getString(R.string.no)) { _, _ -> }
        alertDialog.show()
    }

    private fun clearViews() {
        mTimeTextView.text = getString(R.string.fragment_map_time_default_value)
        mSpeedTextView.text = getString(R.string.fragment_map_speed_default_value)
        mDistanceTextView.text = getString(R.string.fragment_map_distance_default_value)
        mCaloriesTextView.text = getString(R.string.fragment_map_calories_default_value)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
        fun onRequestPermission(pPermission: String, pRequestCode: Int): Boolean
        fun changeBottomNavigationVisibility(pVisibility: Int)
    }

    private enum class GpsSignal(var value: Int) {
        GPS_GOOD(20), GPS_OK(40), GPS_WEAK(60), GPS_BAD(100)
    }
}
