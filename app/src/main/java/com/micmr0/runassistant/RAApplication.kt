package com.micmr0.runassistant

import android.app.Application
import com.micmr0.runassistant.model.Model
import com.micmr0.runassistant.util.ActivePlan

class RAApplication : Application() {
    companion object {
        var sInstance : RAApplication? = null
        var sModel : Model? = null

        var sActivePlan : ActivePlan? = null
    }

    override fun onCreate() {
        super.onCreate()

        sInstance = this
        sModel = Model(applicationContext)
    }

    fun getActivePlan() : ActivePlan? {
        return sActivePlan
    }

    fun setActivePlan(pActivePlan : ActivePlan) {
        sActivePlan = pActivePlan
    }
}