package com.micmr0.runassistant.db

class PlanDayDao {

    var id: Int = 0
    var plan: Int = 0
    var day: Int = 0
    var training: Int = 0
    var date: String? = null
    var type: Int = 0
    var title: String? = null
    var distance: Float = 0.toFloat()
    var time: Int = 0
    var laps: Int = 0
    var tips: Int = 0

    constructor() {
        id = -1
        day = -1
        training = -1
        date = ""
        type = -1
        title = ""
        distance = -1f
        time = -1
        tips = -1
    }

    constructor(planDayId: Int) {
        id = planDayId
        day = -1
        training = -1
        date = ""
        type = -1
        title = ""
        distance = -1f
        time = -1
        tips = -1
    }

}
