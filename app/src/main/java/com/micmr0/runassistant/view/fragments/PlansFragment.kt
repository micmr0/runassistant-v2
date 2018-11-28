package com.micmr0.runassistant.view.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.micmr0.runassistant.Constants
import com.micmr0.runassistant.R
import com.micmr0.runassistant.db.DatabaseHelper
import com.micmr0.runassistant.db.PlanDao
import com.micmr0.runassistant.util.Logger
import com.micmr0.runassistant.util.plansAdapter.PlansAdapter
import com.micmr0.runassistant.view.OnFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_plans.*

class PlansFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var mDbHelper : DatabaseHelper
    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mAdapter: PlansAdapter
    private lateinit var mPlans: ArrayList<PlanDao>
    private lateinit var mNoPlansView : View
    private lateinit var mAddPlanButton : FloatingActionButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.d(this, "onCreateView")
        return inflater.inflate(R.layout.fragment_plans, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Constants.ResultCodes.DELETE_PLAN) {
            mAdapter.removePlan(data!!.getIntExtra(Constants.IntentExtras.PLAN_ID, -1))
            checkPlansView()
        }
        else if(resultCode == Constants.ResultCodes.TRAINING_SET) {
            listener!!.onFragmentInteraction(Uri.parse(Constants.Uri.INITIALIZE_TRAINING))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNoPlansView = fragment_plans_no_plans_view
        mAddPlanButton = fragment_plans_add_plan_button

        mDbHelper = DatabaseHelper(requireContext())

        mRecyclerView = view.findViewById(R.id.fragment_plans_recycler_view) as RecyclerView

        mPlans = mDbHelper.plans as ArrayList<PlanDao>

        mAdapter = PlansAdapter(this, mPlans)
        val mLayoutManager = LinearLayoutManager(context)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.adapter = mAdapter

        checkPlansView()

        mAddPlanButton.setOnClickListener {
            val array = arrayOf("Bieg na 5km \tCel: Ukończyć", "Bieg na 5km \tCel: 45min", "Bieg na 10km \tCel: Ukończyć", "Półmaraton \tCel: Ukończyć")

            val builder = android.support.v7.app.AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.fragment_plans_choose_training_plans))
                    .setItems(array) { _, which ->
                        when (which) {
                            0 -> mDbHelper.createPlan5kmUk()
                            1 -> mDbHelper.createPlan5km45min()
                            2 -> mDbHelper.createPlan10kmUk()
                            3 -> mDbHelper.createPlanPolMaratonUk()
                        }
                        mPlans = mDbHelper.plans as ArrayList<PlanDao>
                        mAdapter.addPlan(mPlans.last())

                        checkPlansView()
                    }
            builder.create()
            builder.show()
        }
    }

    private fun checkPlansView() {
        if(mPlans.isEmpty()) {
            mNoPlansView.visibility = View.VISIBLE
            mRecyclerView.visibility = View.GONE
        } else {
            mNoPlansView.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

        mAdapter.notifyDataSetChanged()
    }
}