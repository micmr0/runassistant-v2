package com.micmr0.runassistant.db

class PlanDao {

    var id: Int = 0
    var table: TableDao? = null

    constructor() : super() {
        id = 0
        table = TableDao()
    }

    constructor(lap_id: Int) : super() {
        id = lap_id
        table = TableDao()
    }
}