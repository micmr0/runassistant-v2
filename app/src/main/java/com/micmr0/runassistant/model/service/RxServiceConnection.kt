package com.micmr0.runassistant.model.service

import android.app.Fragment
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.micmr0.runassistant.util.Logger
import com.micmr0.runassistant.view.UIEvent
import com.micmr0.runassistant.view.fragments.MapFragment
import io.reactivex.Observable

class RxServiceConnection<T : Service, K> {
    /*var connection: ServiceConnection? = null

    fun bind(context: Context, pServiceConnection : ServiceConnection, intent: Intent, pViewEventsObservable: Observable<UIEvent.EventType>?, flag: Int = Context.BIND_AUTO_CREATE): Observable<T> {
        return Observable.create<T> { subscriber ->

            var bound: Boolean = false
            connection = object : ServiceConnection {
                override fun onServiceDisconnected(mName: ComponentName?) {
                    Logger.d(this, "onServiceDisconnected")
                }

                override fun onServiceConnected(mName: ComponentName?, service: IBinder?) {
                    Logger.d(this, "onServiceConnected")
                    service as RxBinder<T,K>

                    (service.getService() as LocationService<*,*>).setUiEventsSubscription(pViewEventsObservable)

                    if(context is Fragment && service is LocationService<*, *>) {
                        (context as MapFragment).setLocationSource(service.getService() as LocationService<*,*>)
                    }

                    if (!subscriber.isDisposed) {
                        subscriber.onNext(service.getService())
                    }
                }
            }
            bound = context.bindService(intent, pServiceConnection, flag)
        }
    }

    fun unbindService(context: Context, pServiceConnection : ServiceConnection) {
        context.unbindService(pServiceConnection)
    }*/
}