package com.micmr0.runassistant.presenter

import android.support.annotation.CallSuper
import com.micmr0.runassistant.model.loaders.Loader
import com.micmr0.runassistant.util.Logger
import com.micmr0.runassistant.view.IView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class Presenter<T, K> {
    private lateinit var mView: IView<T>
    private var mSubscription: Disposable? = null


    protected var mViewEventsSubject : Subject<T>? = null

    @CallSuper
    fun bind(pView: IView<T>) {
        Logger.d(this, "bind()")
        mView = pView

        unBind()

        if (!isSubscribed()) {
            mSubscription = getLoader().getObservable()!!.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { pData ->
                run {
                    if (pData != null) {
                        mView.onGetData(pData)
                    }
                }
            }
        }
    }

    fun getObservable(): Observable<T>? {

        if(mViewEventsSubject == null) {
            mViewEventsSubject = PublishSubject.create()
        }

        return mViewEventsSubject!!.serialize()
    }

    private fun unBind() {
        Logger.d(this, "unBind()")
        if (mSubscription != null && !mSubscription!!.isDisposed) {
            mSubscription!!.dispose()
        }
    }

    private fun isSubscribed(): Boolean {
        return mSubscription != null && !mSubscription!!.isDisposed
    }

    abstract fun getLoader(): Loader<T, K>
}