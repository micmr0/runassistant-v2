package com.micmr0.runassistant.model.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import com.micmr0.runassistant.model.pojo.TrainingData
import com.micmr0.runassistant.util.Logger
import com.micmr0.runassistant.util.SharedPreferencesHelper
import io.reactivex.Observable
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.LocationSource
import com.patloew.rxlocation.RxLocation
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.os.*
import android.support.v4.app.NotificationCompat
import android.text.format.DateFormat
import android.util.TimeUtils
import com.micmr0.runassistant.R
import com.micmr0.runassistant.RAApplication
import com.micmr0.runassistant.db.DatabaseHelper
import com.micmr0.runassistant.util.weather.Weather
import com.micmr0.runassistant.util.weather.WeatherUtil
import com.micmr0.runassistant.view.UIEvent
import com.micmr0.runassistant.view.activities.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class LocationService<T, K> : Service(), LocationSource, LocationSource.OnLocationChangedListener {
    companion object {
        private val NOTIFICATION_ID = 5999
        private val CHANNEL_ID = "YOUR_CHANNEL_ID"
        private val CHANNEL_NAME = "Your human readable notification channel mName"
        private val CHANNEL_DESCRIPTION = "description"
    }

    private val isPlanDay = false
    private var mTrainingId = 0L

    private var mStartTime = 0L
    private var mPauseTime = 0L
    private var mElapsedTime = 0L

    private var mLapId = 0L
    private var mLapStartTime = 0L
    private var mLapAverageSpeed = 0L
    private var mLapRunPointsCount = 0
    private var mLapTime = 0L
    private var mLapSec = 0L
    private var mLapDistance = 0.0f
    private var mLapDistanceLimit = 0.0f

    private var mTrainingRunPointsCount = 0
    private var mDistance = 0.0f
    private var mCalories = 0
    private var mWeight = 0.0
    private var mLastLocation: Location? = null
    private var mAverageSpeed = 0.0f
    private var mMaxSpeed = 0.0f
    private var mMinAltitude = -1.0
    private var mMaxAltitude = 0.0
    private var mWeather: Weather? = null

    private var mMaxDistance = 0.0f
    private var mMaxTime = 0.0f
    private var mMaxLaps = 0

    private var mIsRunning = false

    private lateinit var rxLocation: RxLocation
    private lateinit var locationRequest: LocationRequest

    private lateinit var mHandler: Handler
    private lateinit var mTimeElapsedRunnable: Runnable

    private var mUiEventsSubscription: Disposable? = null

    private lateinit var mDbHelper: DatabaseHelper

    override fun onBind(intent: Intent): IBinder? {
        return MyBinder()
    }

    inner class MyBinder : Binder(), RxBinder<LocationService<T, K>> {
        override fun getService(): LocationService<T, K> = this@LocationService
    }

    override fun onCreate() {
        Logger.d(this, "onCreate")
        super.onCreate()

        mDbHelper = DatabaseHelper(this)

        mLapDistanceLimit = if (SharedPreferencesHelper.getActivityType() == 0) {
            1000f
        } else {
            2000f
        }

        // 1 - marszo-bieg czasowy
        // 2 - marszo-bieg na dystans
        // 3 - dzień szybkosci
        // 4 - próba wyścigu
        // 5 - zawody

        if (RAApplication.sActivePlan != null) {
            when (RAApplication.sActivePlan!!.mActiveDayType) {
                1 -> mMaxTime = RAApplication.sActivePlan!!.mActiveDayValue
                2 -> mMaxDistance = RAApplication.sActivePlan!!.mActiveDayValue
                3 -> {
                    mLapDistance = RAApplication.sActivePlan!!.mActiveDayValue
                    mMaxDistance = mLapDistance * RAApplication.sActivePlan!!.mActiveDayValue2
                }

            }
        }

        mHandler = Handler()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Logger.d(this, "onStartCommand")

        startInForeground()

        return START_NOT_STICKY
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(pLastLocationName: String, pWeatherLocationName: String): Observable<TrainingData>? {

        mTimeElapsedRunnable = Runnable {
            if (mStartTime == 0L) {
                mStartTime = System.currentTimeMillis()
            }

            mElapsedTime = mPauseTime + System.currentTimeMillis() - mStartTime
            mLapTime = System.currentTimeMillis() - mLapStartTime

            mHandler.postDelayed(mTimeElapsedRunnable, 100)
        }


        rxLocation = RxLocation(applicationContext)

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(SharedPreferencesHelper.getLocationPrecision()!!)

        if (!mIsRunning) {
            mHandler.postDelayed(mTimeElapsedRunnable, 100)
            startNewTraining(pLastLocationName, pWeatherLocationName)

            mIsRunning = true
        }

        return rxLocation.location().updates(locationRequest).flatMap { location ->
            if (mLastLocation != null) {
                mDistance = mDistance.plus(location.distanceTo(mLastLocation))
                mLapDistance = mLapDistance.plus(location.distanceTo(mLastLocation))
            }

            if (location.speed > mMaxSpeed) {
                mMaxSpeed = location.speed

            }

            mAverageSpeed += location.speed

            if (mMinAltitude == -1.0) {
                mMinAltitude = location.altitude
            }

            if (location.altitude < mMinAltitude) {
                mMinAltitude = location.altitude
            }

            if (location.altitude > mMaxAltitude) {
                mMaxAltitude = location.altitude
            }

            val distanceInMiles = mDistance * 0.000621371192
            mCalories = (mWeight * 0.75 * distanceInMiles).toInt()

            Logger.d(this, "onNewLocation")
            this.onLocationChanged(location)
            mLastLocation = location

            Observable.just(TrainingData(location, mDistance, mCalories, mElapsedTime))
        }
    }

    private fun startInForeground() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        var activity = if(SharedPreferencesHelper.getActivityType() == 0) {
            getString(R.string.activity_running)
        } else {
            getString(R.string.activity_cycling)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_run)
                .setContentTitle(activity)
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(pendingIntent)
        val notification = builder.build()

        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESCRIPTION
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        startForeground(NOTIFICATION_ID, notification)
    }

    fun setUiEventsSubscription(pObservable: Observable<UIEvent.EventType>?) {
        mUiEventsSubscription = pObservable?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe { pData ->
            run {
                if (pData != null) {
                    Logger.d(this, "pData != null")
                    when (pData) {
                        UIEvent.EventType.START_LOGGING -> {
                            Logger.d(this, "startLogging")
                            mHandler.postDelayed(mTimeElapsedRunnable, 100)
                            //startNewTraining()
                        }

                        UIEvent.EventType.PAUSE_LOGGING -> {
                            Logger.d(this, "pauseLogging")

                            mHandler.removeCallbacks(mTimeElapsedRunnable)
                            mPauseTime = mElapsedTime
                            mStartTime = 0L
                        }

                        UIEvent.EventType.RESUME_LOGGING -> {
                            Logger.d(this, "resumeLogging")
                            mHandler.postDelayed(mTimeElapsedRunnable, 100)

                        }

                        UIEvent.EventType.STOP_LOGGING -> {
                            mHandler.removeCallbacks(mTimeElapsedRunnable)
                            saveLap()
                            saveTraining()
                            mIsRunning = false
                            stopSelf()
                        }
                    }
                }
            }
        }
    }

    private fun startNewTraining(pLastLocationName: String, pWeatherLocationName: String) {
        eraseValues()

        val kg = SharedPreferencesHelper.getWeight()!!.toDouble()
        mWeight = kg * 2.2046

        val trainingName = checkTrainingName()

        if (!pWeatherLocationName.isEmpty()) {
            GetWeather(applicationContext).execute(pWeatherLocationName)
        }


        mTrainingId = if (RAApplication.sActivePlan == null) {
            mDbHelper.startNewTraining(trainingName, pLastLocationName)
        } else {
            mDbHelper.startNewTraining(RAApplication.sActivePlan!!.mActiveDayTitle, pLastLocationName)
        }

        startNewLap()
    }

    inner class GetWeather(pContext: Context) : AsyncTask<String, String, String>() {
        @SuppressLint("StaticFieldLeak")
        private val mContext = pContext

        override fun doInBackground(vararg pValues: String?): String {
            mWeather = Weather()
            mWeather = WeatherUtil.getWeather(mContext, pValues[0].toString())

            return ""
        }
    }

    private fun checkTrainingName(): String {
        var name = ""
        val calendar = Calendar.getInstance()

        when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..5 -> {
                name = baseContext.getString(R.string.training_name_night)
            }
            in 6..11 -> {
                name = baseContext.getString(R.string.training_name_morning)
            }
            in 12..18 -> {
                name = baseContext.getString(R.string.training_name_afternoon)
            }
            in 19..23 -> {
                name = baseContext.getString(R.string.training_name_evening)
            }
        }
        name += String.format(" (%s)", DateFormat.format("HH:mm", calendar.timeInMillis).toString())

        return name
    }

    private fun startNewLap() {
        mLapId = mDbHelper.startNewLap(mTrainingId)
        Logger.d(this, "lap mId is: : $mLapId")
        mLapStartTime = System.currentTimeMillis()
        mLapDistance = 0.0f
        mLapAverageSpeed = 0
        mLapRunPointsCount = 0
        mLapTime = 0
        mLapSec = 0
    }

    private fun saveLap() {
        Logger.d(this, "lapSec: $mLapSec")
        if (mLapRunPointsCount != 0) {
            mLapAverageSpeed = mLapAverageSpeed / mLapRunPointsCount
        }
        mDbHelper.saveLap(mLapId, mLapTime, mLapAverageSpeed)

        //TrainingLaps.addLapView(lapSec)
    }

    private fun saveTraining() {
        Logger.d(this, "saveTraining")

        if (mTrainingRunPointsCount > 0) {
            mAverageSpeed /= mTrainingRunPointsCount
        } else {
            mAverageSpeed = 0.0f
        }

        if (mWeather != null) {
            mDbHelper.saveTraining(mTrainingId, mElapsedTime.toInt(), mDistance, mAverageSpeed, mMaxSpeed, mMinAltitude, mMaxAltitude, mCalories, mWeather!!)
        } else {
            mDbHelper.saveTraining(mTrainingId, mElapsedTime.toInt(), mDistance, mAverageSpeed, mMaxSpeed, mMinAltitude, mMaxAltitude, mCalories)
        }

        if (RAApplication.sActivePlan != null) {
            mDbHelper.savePlanDay(RAApplication.sActivePlan!!.mPlanId, RAApplication.sActivePlan!!.mActiveDay, mTrainingId)
        }
    }

    private fun eraseValues() {
        mStartTime = 0L
        mPauseTime = 0L
        mElapsedTime = 0L

        mLapId = 0L
        mLapStartTime = 0L
        mLapAverageSpeed = 0L
        mLapRunPointsCount = 0
        mLapTime = 0L
        mLapSec = 0L
        mLapDistance = 0.0f

        mTrainingRunPointsCount = 0
        mDistance = 0.0f
        mCalories = 0
        mWeight = 0.0
        mLastLocation = null
        mAverageSpeed = 0.0f
        mMaxSpeed = 0.0f
        mMinAltitude = -1.0
        mMaxAltitude = 0.0
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d(this, "onDestroy")
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Logger.d(this, "onRebind")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Logger.d(this, "onTrimMemory")
    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {
        // NOTHING TO DO
    }

    override fun deactivate() {
        // NOTHING TO DO
    }

    override fun onLocationChanged(pLocation: Location) {
        mTrainingRunPointsCount++

        mDbHelper.insertNewRunpoint(mLapId, pLocation)

        if (mLapDistance >= mLapDistanceLimit) {
            saveLap()
            startNewLap()
        }

        if ((mMaxDistance > 0 && mDistance > mMaxDistance) || (mMaxTime > 0 && mElapsedTime > TimeUnit.MINUTES.toMillis(mMaxTime.toLong()))) {
            mHandler.removeCallbacks(mTimeElapsedRunnable)
            saveLap()
            saveTraining()
            mIsRunning = false
            stopSelf()
        }
    }
}