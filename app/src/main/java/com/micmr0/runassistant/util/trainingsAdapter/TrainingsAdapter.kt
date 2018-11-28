package com.micmr0.runassistant.util.trainingsAdapter

import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import com.micmr0.runassistant.db.TrainingDao
import android.widget.TextView
import com.micmr0.runassistant.R
import android.view.LayoutInflater
import android.widget.ImageView
import com.micmr0.runassistant.Constants
import com.micmr0.runassistant.view.activities.TrainingSummaryActivity
import com.micmr0.runassistant.view.fragments.HistoryFragment
import java.util.concurrent.TimeUnit

class TrainingsAdapter(pFragment : HistoryFragment, pTrainingsList : List<ListItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    var mTrainingsList = pTrainingsList
    var mFragment = pFragment


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == TYPE_HEADER) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.training_header, parent, false)
            HeaderViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.training_item, parent, false)
            ItemViewHolder(itemView)
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is HeaderViewHolder) {
            val currentItem = mTrainingsList[position] as Header
            holder.title.text = currentItem.mName

        } else if(holder is ItemViewHolder) {
            val currentItem = mTrainingsList[position] as HistoryItem

            holder.trainingId = currentItem.mTrainingDao.mId

            holder.title.text = currentItem.mTrainingDao.mName
            holder.location.text = currentItem.mTrainingDao.mLocation

            if (currentItem.mTrainingDao.mType == TrainingDao.TrainingType.RUNNING) {
                holder.image.setImageResource(R.drawable.ic_run)
            } else {
                holder.image.setImageResource(R.drawable.ic_cycling)
            }

            val creationDate =  currentItem.mTrainingDao.mCreationDate!!.toLong()
            val creationDateLabel  = DateFormat.format("HH:mm:ss", creationDate).toString()

            holder.distance.text = mFragment.getString(R.string.training_distance_value, Math.round(currentItem.mTrainingDao.mDistance)/1000, Math.round(currentItem.mTrainingDao.mDistance)%1000)

            holder.title.text = currentItem.mTrainingDao.mName
            holder.duration.text = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours( currentItem.mTrainingDao.mTime.toLong()),
                                                                            TimeUnit.MILLISECONDS.toMinutes( currentItem.mTrainingDao.mTime.toLong()),
                                                                            TimeUnit.MILLISECONDS.toSeconds( currentItem.mTrainingDao.mTime.toLong()) % 60)

            holder.calories.text = currentItem.mTrainingDao.mCalories.toString()

            holder.container.setOnClickListener { showTrainingSummary(holder.trainingId) }
        }
    }

    private fun showTrainingSummary(pTrainingId : Long) {
        val intent = Intent(mFragment.context, TrainingSummaryActivity::class.java)
        intent.putExtra(Constants.IntentExtras.TRAINING_ID, pTrainingId)

        mFragment.startActivityForResult(intent, Constants.RequestCodes.DEFAULT)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return mTrainingsList[position] is Header

    }

    override fun getItemCount(): Int {
        return mTrainingsList.size
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.training_header_title)

    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var trainingId = 0L
        var container : CardView = view.findViewById(R.id.training_item_container)
        var image: ImageView = view.findViewById(R.id.training_item_image_view)
        var title: TextView = view.findViewById(R.id.training_item_title)
        var duration: TextView = view.findViewById(R.id.training_item_duration)
        var distance: TextView = view.findViewById(R.id.training_item_distance)
        var calories: TextView = view.findViewById(R.id.training_item_calories)
        var location: TextView = view.findViewById(R.id.training_item_location)
    }
}