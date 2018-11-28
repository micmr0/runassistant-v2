package com.micmr0.runassistant.util.weather

class Weather {
    lateinit var mCurrentCondition : CurrentCondition
    lateinit var mLocation : Location
    lateinit var mTemperature: Temperature
    lateinit var mWind : Wind
    var mClouds = 0
    var mImageByteArray: ByteArray? = null
}