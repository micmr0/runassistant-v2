package com.micmr0.runassistant.model.service

import android.app.Service
import android.os.IBinder

interface RxBinder<T : Service> : IBinder {
    fun getService(): T
}