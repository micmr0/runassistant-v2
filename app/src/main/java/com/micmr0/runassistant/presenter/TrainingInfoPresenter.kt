package com.micmr0.runassistant.presenter

import com.micmr0.runassistant.RAApplication
import com.micmr0.runassistant.model.loaders.LocationLoader
import com.micmr0.runassistant.model.pojo.TrainingData
import com.micmr0.runassistant.view.UIEvent

class TrainingInfoPresenter : Presenter<TrainingData, UIEvent.EventType>() {
    override fun getLoader(): LocationLoader {
        return RAApplication.sModel!!.mLocationLoader
    }

}