package com.micmr0.runassistant.util.plansAdapter

import android.content.Intent
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.micmr0.runassistant.R
import android.view.LayoutInflater
import android.widget.ImageView
import com.micmr0.runassistant.Constants
import com.micmr0.runassistant.db.PlanDao
import com.micmr0.runassistant.view.activities.PlanActivity
import com.micmr0.runassistant.view.fragments.PlansFragment

class PlansAdapter(pFragment : PlansFragment, pPlansList : ArrayList<PlanDao>) : RecyclerView.Adapter<PlansAdapter.ViewHolder>() {
    var mFragment = pFragment
    var mPlans = pPlansList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.planview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlansAdapter.ViewHolder, position: Int) {

        when(mPlans[position].table!!.title) {
            PlanType.KM5.mDescription -> holder.ivIcon.background = AppCompatResources.getDrawable(mFragment.requireContext(), R.drawable.run5km)
            PlanType.KM10.mDescription -> holder.ivIcon.background = AppCompatResources.getDrawable(mFragment.requireContext(), R.drawable.run10km)
            PlanType.HALF_MAR.mDescription -> holder.ivIcon.background = AppCompatResources.getDrawable(mFragment.requireContext(), R.drawable.run_half_mar)
            PlanType.MAR.mDescription -> holder.ivIcon.background = AppCompatResources.getDrawable(mFragment.requireContext(), R.drawable.ic_run)
        }

        holder.tvTitle.text =  mPlans[position].table!!.title
        holder.tvDescription.text = mFragment.getString(R.string.goal, mPlans[position].table!!.goal)
        holder.tvDays.text = mFragment.getString(R.string.how_many_plan_days, mPlans[position].table!!.days)

        holder.container.setOnClickListener {

            val intent = Intent(mFragment.requireContext(), PlanActivity::class.java)
            intent.putExtra("PLAN_ID", mPlans[position].id)
            mFragment.startActivityForResult(intent, Constants.ResultCodes.DELETE_PLAN)
        }

    }

    fun addPlan(pPlan : PlanDao) {
        mPlans.add(pPlan)
        notifyDataSetChanged()
    }

    fun removePlan(pPlanId : Int) {
        for(planDao in mPlans) {
            if(planDao.id == pPlanId) {
                mPlans.remove(planDao)
                break
            }
        }

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mPlans.size
    }

    class ViewHolder(pView : View) : RecyclerView.ViewHolder(pView) {
        var container = pView.findViewById<View>(R.id.plan_view_item_container)
        var ivIcon= pView.findViewById<View>(R.id.plan_view_item_icon_text_view) as ImageView
        var tvTitle = pView.findViewById<View>(R.id.plan_view_item_title_text_view) as TextView
        var tvDescription= pView.findViewById<View>(R.id.plan_view_item_description_text_view) as TextView
        var tvDays = pView.findViewById<View>(R.id.tvDays) as TextView
    }

    enum class PlanType(pPlanDescription : String) {
        KM5("Bieg na 5 km"), KM10("Bieg na 10 km"), HALF_MAR("Półmaraton"), MAR("Maraton");

        var mDescription = pPlanDescription
    }

}