package com.micmr0.runassistant.db

class TableDao {

    var id: Long = 0
    var title: String? = null
    var goal: String? = null
    var description: String? = null
    var days: Int = 0

    constructor() : super() {
        id = 0
        title = ""
        goal = ""
        description = ""
        days = 0
    }

    constructor(lap_id: Long) : super() {
        id = lap_id
        title = ""
        goal = ""
        description = ""
        days = 0
    }
}
