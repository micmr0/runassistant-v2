package com.micmr0.runassistant.view.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.micmr0.runassistant.R
import com.micmr0.runassistant.util.Logger
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateFormat
import android.widget.TextView
import com.micmr0.runassistant.Constants
import com.micmr0.runassistant.db.DatabaseHelper
import com.micmr0.runassistant.db.TrainingDao
import com.micmr0.runassistant.util.trainingsAdapter.Header
import com.micmr0.runassistant.util.trainingsAdapter.HistoryItem
import com.micmr0.runassistant.util.trainingsAdapter.ListItem
import com.micmr0.runassistant.util.trainingsAdapter.TrainingsAdapter
import com.micmr0.runassistant.view.OnFragmentInteractionListener
import java.util.*
import kotlin.collections.ArrayList


class HistoryFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var mDbHelper : DatabaseHelper

    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mNoTraingsView : View
    private lateinit var mAdapter: TrainingsAdapter

    private lateinit var mTrainingList : List<TrainingDao>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.d(this, "onCreateView")
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDbHelper = DatabaseHelper(requireContext())

        mRecyclerView = view.findViewById(R.id.fragment_history_recycler_view)
        mNoTraingsView = view.findViewById(R.id.fragment_history_no_trainings_view)

        mTrainingList = mDbHelper.trainings

        if(mTrainingList.isEmpty()) {
            mNoTraingsView.visibility = View.VISIBLE
            mRecyclerView.visibility = View.GONE

        }

        val trainingAdapterList = prepareAdapterData(mTrainingList)

        mAdapter = TrainingsAdapter(this, trainingAdapterList)
        val mLayoutManager = LinearLayoutManager(context)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.adapter = mAdapter
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

    private fun prepareAdapterData(pTrainingList : List<TrainingDao>) : List<ListItem> {
        if(!pTrainingList.isEmpty()) {
            val adapterList = ArrayList<ListItem>()

            val date = pTrainingList.first().mCreationDate!!.toLong()
            var mLastDate = Calendar.getInstance()
            mLastDate.timeInMillis = date

            val creationDateLabel  = DateFormat.format("dd-MM-yyyy", mLastDate).toString()

            adapterList.add(Header(creationDateLabel))

            for(item in pTrainingList) {
                val itemDate = Calendar.getInstance()
                itemDate.timeInMillis = item.mCreationDate!!.toLong()

                if(mLastDate.get(Calendar.YEAR) != itemDate.get(Calendar.YEAR) || mLastDate.get(Calendar.DAY_OF_YEAR) != itemDate.get(Calendar.DAY_OF_YEAR)) {
                    adapterList.add(Header(DateFormat.format("dd-MM-yyyy", itemDate).toString()))
                    mLastDate = itemDate
                }

                adapterList.add(HistoryItem(item))
            }
            return adapterList
        }
       return ArrayList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Constants.ResultCodes.DELETE_TRAINING) {
            listener!!.onFragmentInteraction(Uri.parse(Constants.Uri.INITIALIZE_TRAINING))
        }
    }


}