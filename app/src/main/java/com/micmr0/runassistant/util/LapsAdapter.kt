package com.micmr0.runassistant.util

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.micmr0.runassistant.R
import com.micmr0.runassistant.db.LapDao
import java.util.concurrent.TimeUnit

class LapsAdapter(pContext: Context, pLapsList: List<LapDao>) : RecyclerView.Adapter<LapsAdapter.ItemViewHolder>() {

    var mLapsList = pLapsList
    var mContext = pContext
    var lastTime = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lap_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (position > 0) {
            lastTime += mLapsList[position - 1].time
        }

        holder.number.text = String.format("%d.", position + 1)
        holder.tempo.text = String.format("%02d:%02d min/km", TimeUnit.MILLISECONDS.toMinutes(mLapsList[position].time.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(mLapsList[position].time.toLong()) % 60)

        val time = mLapsList[position].time + lastTime

        holder.time.text = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time.toLong()),
                TimeUnit.MILLISECONDS.toMinutes(time.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(time.toLong()) % 60)
    }

    override fun getItemCount(): Int {
        return mLapsList.size
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var number: TextView = view.findViewById(R.id.lap_item_number_text_view)
        var tempo: TextView = view.findViewById(R.id.lap_item_tempo_text_view)
        var time: TextView = view.findViewById(R.id.lap_item_time_text_view)

    }
}