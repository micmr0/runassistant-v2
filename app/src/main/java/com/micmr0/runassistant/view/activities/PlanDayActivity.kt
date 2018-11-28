package com.micmr0.runassistant.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.micmr0.runassistant.Constants
import com.micmr0.runassistant.Constants.IntentExtras.Companion.DAY
import com.micmr0.runassistant.Constants.IntentExtras.Companion.PLAN_ID
import com.micmr0.runassistant.R
import com.micmr0.runassistant.RAApplication
import com.micmr0.runassistant.db.DatabaseHelper
import com.micmr0.runassistant.db.PlanDayDao
import com.micmr0.runassistant.util.ActivePlan

class PlanDayActivity : BaseActivity() {
    private var dBHelper: DatabaseHelper? = null
    private var planId: Int = 0
    private var day: Int = 0
    private var planDay: PlanDayDao? = null
    private var tvTitle: TextView? = null
    private var tvDescription: TextView? = null
    private var tipDescription: String? = null
    private var btnSetTraining: Button? = null
    internal lateinit var i: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mToolbar.setNavigationOnClickListener { onBackPressed() }

        tvTitle = findViewById<View>(R.id.plan_day_view_item_title_text_view) as TextView
        tvDescription = findViewById<View>(R.id.plan_day_view_item_description_text_view) as TextView
        btnSetTraining = findViewById<View>(R.id.btnSetTraining) as Button

        dBHelper = DatabaseHelper(this)

        val bundle = intent.extras
        planId = bundle!!.getInt(PLAN_ID)
        day = bundle.getInt(DAY)

        planDay = dBHelper!!.getPlanDay(planId, day)
        tipDescription = dBHelper!!.getTip(planDay!!.tips.toLong())

        title = getString(R.string.plan_day_number, planDay!!.day)

        tvTitle!!.text = planDay!!.title
        tvDescription!!.text = tipDescription

        if (planDay!!.type == 0) {
            if (planDay!!.date == null) {
                btnSetTraining!!.text = getString(R.string.set_as_done)
            } else {
                btnSetTraining!!.text = getString(R.string.training_date, planDay!!.date!!)
                btnSetTraining!!.isEnabled = false
            }

        } else {
            if (planDay!!.date == null) {
                btnSetTraining!!.text = getString(R.string.set_training)
            }
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_plan_day
    }

    override fun onPermissionGranted(pPermission: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun setTraining(view: View) {
        if (planDay!!.type == 0) {

            Log.d("plan", "planId: " + planId + "day: " + day)

            val date = dBHelper!!.setPlanDayDate(planId, day)

            btnSetTraining!!.text = "Wykonano: $date"
            btnSetTraining!!.isEnabled = false

        } else {
            // PlanDays type:
            // 0 - Wolne lub TU
            // 1 - marszo-bieg czasowy
            // 2 - marszo-bieg na dystans
            // 3 - dzień szybkosci
            // 4 - próba wyścigu
            // 5 - zawody

            val activePlan = ActivePlan(planId, day, planDay!!.title!!, planDay!!.type)

            if (planDay!!.type == 1) {
                activePlan.mActiveDayValue = planDay!!.time.toFloat()
            } else if (planDay!!.type == 2) {
                activePlan.mActiveDayValue = planDay!!.distance
            } else if (planDay!!.type == 3) {
                activePlan.mActiveDayValue = planDay!!.distance
                activePlan.mActiveDayValue2 = planDay!!.laps
            } else if (planDay!!.type == 4) {
                activePlan.mActiveDayValue = planDay!!.distance
            } else if (planDay!!.type == 5) {
                activePlan.mActiveDayValue = planDay!!.distance
            }

            RAApplication.sInstance!!.setActivePlan(activePlan)

            setResult(Constants.ResultCodes.TRAINING_SET)

            finish()
        }
    }
}