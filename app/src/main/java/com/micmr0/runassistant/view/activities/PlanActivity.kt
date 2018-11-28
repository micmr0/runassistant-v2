package com.micmr0.runassistant.view.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import com.micmr0.runassistant.Constants
import com.micmr0.runassistant.R
import com.micmr0.runassistant.db.DatabaseHelper
import com.micmr0.runassistant.db.PlanDao
import com.micmr0.runassistant.db.PlanDayDao
import com.micmr0.runassistant.util.planDayAdapter.PlanDaysAdapter
import java.util.ArrayList

class PlanActivity : BaseActivity() {
    private var toolbar: Toolbar? = null
    private var planId: Int = 0
    private lateinit var mDbHelper: DatabaseHelper
    private var lvPlandays: ListView? = null
    private var mAdapter: PlanDaysAdapter? = null

    private var tvGoal: TextView? = null
    private var tvDays: TextView? = null
    private var tvDescription: TextView? = null

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mPlanDay: ArrayList<PlanDayDao>

    private var plan: PlanDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        planId = bundle!!.getInt("PLAN_ID")

        tvGoal = findViewById<View>(R.id.tvGoal) as TextView
        tvDays = findViewById<View>(R.id.tvDays) as TextView
        tvDescription = findViewById<View>(R.id.tvDescription) as TextView

        mRecyclerView = findViewById<RecyclerView>(R.id.rv_plandays)

        mDbHelper = DatabaseHelper(this)

        plan = mDbHelper.getPlan(planId)
        mPlanDay = mDbHelper.getPlanDays(planId)

        mAdapter = PlanDaysAdapter(this, mPlanDay)
        val mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.adapter = mAdapter

        title = "Plan - " + plan!!.table!!.title!!

        tvGoal!!.text = getString(R.string.goal, plan!!.table!!.goal!!)
        tvDays!!.text = getString(R.string.how_many_plan_days, plan!!.table!!.days)
        tvDescription!!.text = plan!!.table!!.description

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_plan, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_delete_plan) {

            val alertDialog = AlertDialog.Builder(
                    this)

            alertDialog.setPositiveButton(getString(R.string.training_end_yes)) { _, _ ->
                val databaseHelper = DatabaseHelper(baseContext)
                databaseHelper.deletePlan(planId)

                setResult(Constants.ResultCodes.DELETE_PLAN, Intent().putExtra(Constants.IntentExtras.PLAN_ID, planId))
                finish()
            }

            alertDialog.setNegativeButton(getString(R.string.training_end_cancel), null)
            alertDialog.setMessage(getString(R.string.delete_plan_message))
            alertDialog.setTitle(getString(R.string.delete_plan_title))
            alertDialog.show()

            return true
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Constants.ResultCodes.TRAINING_SET) {
            setResult(Constants.ResultCodes.TRAINING_SET)
            finish()
        }
    }


    override fun onPermissionGranted(pPermission: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_plan
    }

    fun showPlanInfo(view: View) {

        AlertDialog.Builder(this@PlanActivity)
                .setTitle("Informacje")
                .setMessage("Plan treningowy na podstawie ksiązki \"Bieganie metodą Gallowaya.\" \n\n Autor: Jeff Galloway")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, which ->
                    // Whatever...
                }.create().show()
    }

}
