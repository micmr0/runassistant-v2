package com.micmr0.runassistant.view.fragments

import android.os.Bundle
import android.os.TestLooperManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.micmr0.runassistant.R
import com.micmr0.runassistant.db.DatabaseHelper
import com.micmr0.runassistant.util.Logger
import java.util.concurrent.TimeUnit

class AchievementsFragment : Fragment() {
    private lateinit var mDbHelper: DatabaseHelper

    private lateinit var mDistanceWeekTextView: TextView
    private lateinit var mTimeWeekTextView: TextView
    private lateinit var mCaloriesWeekTextView: TextView

    private lateinit var mDistanceMonthTextView: TextView
    private lateinit var mTimeMonthTextView: TextView
    private lateinit var mCaloriesMonthTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.d(this, "onCreateView")
        return inflater.inflate(R.layout.fragment_achievements, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDistanceWeekTextView = view.findViewById(R.id.fragment_achievements_week_distance_text_view)
        mTimeWeekTextView = view.findViewById(R.id.fragment_achievements_week_time_text_view)
        mCaloriesWeekTextView = view.findViewById(R.id.fragment_achievements_week_calories_text_view)

        mDistanceMonthTextView = view.findViewById(R.id.fragment_achievements_month_distance_text_view)
        mTimeMonthTextView = view.findViewById(R.id.fragment_achievements_month_time_text_view)
        mCaloriesMonthTextView = view.findViewById(R.id.fragment_achievements_month_calories_text_view)

        mDbHelper = DatabaseHelper(requireContext())

        val weekAchievement = mDbHelper.getWeekAchievments()
        val monthAchievement = mDbHelper.getMonthAchievments()

        mDistanceWeekTextView.text = getString(R.string.training_distance_value, (weekAchievement.distance / 1000).toInt(), (weekAchievement.distance % 1000).toInt())
        mTimeWeekTextView.text = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(weekAchievement.time.toLong()),
                TimeUnit.MILLISECONDS.toMinutes(weekAchievement.time.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(weekAchievement.time.toLong()) % 60)
        mCaloriesWeekTextView.text = weekAchievement.calories.toString()

        mDistanceMonthTextView.text = getString(R.string.training_distance_value, (weekAchievement.distance / 1000).toInt(), (weekAchievement.distance % 1000).toInt())
        mTimeMonthTextView.text = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(weekAchievement.time.toLong()),
                TimeUnit.MILLISECONDS.toMinutes(monthAchievement.time.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(monthAchievement.time.toLong()) % 60)
        mCaloriesMonthTextView.text = monthAchievement.calories.toString()


    }

}