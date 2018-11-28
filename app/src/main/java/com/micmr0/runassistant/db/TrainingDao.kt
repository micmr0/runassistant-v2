package com.micmr0.runassistant.db

class TrainingDao {
    var mId: Long = 0
    var mName: String? = null
    var mCreationDate: String? = null
    var mYear: String? = null
    var mMonth: String? = null
    var mWeekOfYear: String? = null
    var mType: TrainingType? = null
    var mTime: Int = 0
    var mDistance: Float = 0.toFloat()
    var mMinSpeed: Int = 0
    var mAverageSpeed: Int = 0
    var mMaxSpeed: Int = 0
    var mMinAltitude: Int = 0
    var mMaxAltitude: Int = 0
    var mLocation: String? = null
    var mCalories: Int = 0
    var mWeatherTemperature = 0.0f
    var mWeatherWindSpeed = 0.0f
    var mWeatherHumidity = 0
    var mWeatherPressure = 0
    var mWeatherIcon = ""
    var mWeatherDescription = ""

    var mLaps: List<LapDao>? = null

    constructor() : super()

    constructor(training_id: Long) : super() {
        mId = training_id
        mName = ""
        mCreationDate = ""
        mType = TrainingType.RUNNING
        mTime = 0
        mDistance = 0f
        mMinSpeed = 0
        mAverageSpeed = 0
        mMaxSpeed = 0
        mMinAltitude = 0
        mMaxAltitude = 0
        mLocation = ""
        mCalories = 0
    }

    enum class TrainingType {
        RUNNING, CYCLING
    }
}