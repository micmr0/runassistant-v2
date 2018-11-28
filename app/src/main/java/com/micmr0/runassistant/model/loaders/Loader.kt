package com.micmr0.runassistant.model.loaders

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class Loader<T, K> {

    protected lateinit var mSubscription : Disposable
    protected var mSubject : Subject<T>? = null
    protected var mViewEventsSubject : Subject<K>? = null

    fun getObservable(): Observable<T>? {

        if(mSubject == null) {
            mSubject = PublishSubject.create()
        }

        return mSubject!!.serialize()
    }

    fun getViewEventsSubject() : Observable<K>?  {
        if(mViewEventsSubject == null) {
            mViewEventsSubject = PublishSubject.create()
        }

        return mViewEventsSubject!!.serialize()
    }
}