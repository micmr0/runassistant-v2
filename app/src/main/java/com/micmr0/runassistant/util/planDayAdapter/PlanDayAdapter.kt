package com.micmr0.runassistant.util.planDayAdapter

import android.app.Activity
import android.content.Intent
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.micmr0.runassistant.Constants
import com.micmr0.runassistant.Constants.IntentExtras.Companion.DAY
import com.micmr0.runassistant.Constants.IntentExtras.Companion.PLAN_ID
import com.micmr0.runassistant.R
import com.micmr0.runassistant.db.PlanDayDao
import com.micmr0.runassistant.view.activities.PlanDayActivity
import com.micmr0.runassistant.view.activities.TrainingSummaryActivity

class PlanDaysAdapter(pActivity : Activity, pPlanDays : ArrayList<PlanDayDao>) : RecyclerView.Adapter<PlanDaysAdapter.ViewHolder>() {
    private var mActivity = pActivity
    private var mPlanDays = pPlanDays


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanDaysAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.planday_view_item, parent, false)
        return PlanDaysAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanDaysAdapter.ViewHolder, position: Int) {

        if (mPlanDays[position].date == null) {
            holder.ivIcon.setImageDrawable(AppCompatResources.getDrawable(mActivity, R.drawable.planday))
        } else {
            holder.ivIcon.setImageDrawable(AppCompatResources.getDrawable(mActivity, R.drawable.ic_done))
        }

        holder.tvDay.text = mActivity.getString(R.string.plan_day_number, mPlanDays[position].day)
        holder.tvTitle.text = mPlanDays[position].title

        holder.container.setOnClickListener {
            if (mPlanDays[position].training == 0) { //trening nie zostal wykonany

                val intent = Intent(mActivity, PlanDayActivity::class.java)
                intent.putExtra(PLAN_ID, mPlanDays[position].plan)
                intent.putExtra(DAY, mPlanDays[position].day)
                mActivity.startActivityForResult(intent, Constants.RequestCodes.TRAINING_SET)

            } else { //trening wykonany

                val intent = Intent(mActivity, TrainingSummaryActivity::class.java)
                intent.putExtra(Constants.IntentExtras.TRAINING_ID, mPlanDays[position].training.toLong())
                mActivity.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return mPlanDays.size
    }

    class ViewHolder(pView: View) : RecyclerView.ViewHolder(pView) {
        var container = pView.findViewById<View>(R.id.plan_day_view_container) as CardView
        var ivIcon = pView.findViewById<View>(R.id.plan_day_view_item_icon_text_view) as ImageView
        var tvDay = pView.findViewById<View>(R.id.plan_day_view_item_day_text_view) as TextView
        var tvTitle = pView.findViewById<View>(R.id.plan_day_view_item_title_text_view) as TextView
    }
}
