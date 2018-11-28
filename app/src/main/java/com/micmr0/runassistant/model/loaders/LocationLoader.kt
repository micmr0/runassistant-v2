package com.micmr0.runassistant.model.loaders

import android.app.Fragment
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import com.micmr0.runassistant.model.pojo.TrainingData
import com.micmr0.runassistant.util.Logger
import com.micmr0.runassistant.model.service.LocationService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.support.v4.content.ContextCompat.startForegroundService
import android.os.Build
import android.os.IBinder
import com.micmr0.runassistant.model.service.RxBinder
import com.micmr0.runassistant.view.UIEvent
import com.micmr0.runassistant.view.fragments.MapFragment
import io.reactivex.Observable

class LocationLoader(pContext: Context) : Loader<TrainingData, UIEvent.EventType>() {
    private var mContext: Context = pContext

    private var serviceIntent: Intent
    private var mConnection: ServiceConnection? = null
    private var mServiceRunning = false

    init {
        Logger.d(this, "LocationLoader init")

        serviceIntent = Intent(mContext, LocationService::class.java)


    }

    private fun bindService(): Observable<LocationService<TrainingData, UIEvent>> {
        return Observable.create<LocationService<TrainingData, UIEvent>> { subscriber ->

            var bound: Boolean = false
            mConnection = object : ServiceConnection {
                override fun onServiceDisconnected(name: ComponentName?) {
                    Logger.d(this, "onServiceDisconnected")
                    stopLogging()
                }

                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    Logger.d(this, "onServiceConnected")
                    service as RxBinder<LocationService<TrainingData, UIEvent>>

                    (service.getService() as LocationService<*,*>).setUiEventsSubscription(getViewEventsSubject())

                    if(mContext is Fragment && service is LocationService<*,*>) {
                        (mContext as MapFragment).setLocationSource(service.getService() as LocationService<*,*>)
                    }

                    if (!subscriber.isDisposed) {
                        subscriber.onNext(service.getService())
                    }
                }
            }
            bound = mContext.bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindService() {
        mContext.unbindService(mConnection)
    }

    private fun onNext(pData: TrainingData) {
        Logger.d(this, "onNext")
        mSubject?.onNext(pData)
    }

    private fun onError(pData: Throwable) {
        Logger.d(this, "onError")
        if (mSubject != null && mSubject!!.hasObservers()) {
            mSubject!!.onError(pData)
        }
    }

    private fun onComplete() {
        Logger.d(this, "onComplete")
        if (mSubject != null && mSubject!!.hasObservers()) {
            mSubject!!.onComplete()
        }
    }

    private fun initLogging() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mContext, serviceIntent)
        } else {
            mContext.startService(serviceIntent)
        }

        mServiceRunning = true
    }

    fun startLogging(pLastLocationName : String, pWeatherLocationName : String) {

        if (!mServiceRunning) {
            initLogging()
        }

        Logger.d(this, "startLogging")

        mSubscription = bindService()
                .flatMap { service ->
                    service.startLocationUpdates(pLastLocationName, pWeatherLocationName)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onNext, ::onError, ::onComplete)
    }

    fun pauseLogging() {
        Logger.d(this, "pauseLogging")

        mSubscription.dispose()
        mViewEventsSubject?.onNext(UIEvent.EventType.PAUSE_LOGGING)

        unbindService()
    }

    fun resumeLogging(pLastLocationName : String, pWeatherLocationName : String) {
        Logger.d(this, "resumeLogging")

        mSubscription = bindService()
                .flatMap { service ->
                    service.startLocationUpdates(pLastLocationName, pWeatherLocationName)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onNext, ::onError, ::onComplete)

        mViewEventsSubject?.onNext(UIEvent.EventType.RESUME_LOGGING)
    }

    fun stopLogging() {
        mViewEventsSubject?.onNext(UIEvent.EventType.STOP_LOGGING)
        mServiceRunning = false
    }
}