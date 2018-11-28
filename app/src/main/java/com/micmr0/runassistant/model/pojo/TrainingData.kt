package com.micmr0.runassistant.model.pojo

import android.location.Location

class TrainingData(pLocation: Location, pDistance: Float, pCalories: Int, pElapsedTime : Long) {
    var mLocation : Location = pLocation
    var mDistance : Float = pDistance
    var mCalories : Int = pCalories
    var mElapsedTime : Long = pElapsedTime
}