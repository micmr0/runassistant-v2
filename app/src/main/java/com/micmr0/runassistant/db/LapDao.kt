package com.micmr0.runassistant.db

class LapDao {
    var id: Long = 0
    var training_id: Long = 0
    var time: Int = 0
    var average_speed: Float = 0.toFloat()

    constructor() : super() {
        id = 0
        training_id = 0
        time = 0
        average_speed = 0f
    }

    constructor(lap_id: Long) : super() {
        id = lap_id
        training_id = 0
        time = 0
        average_speed = 0f
    }

}
