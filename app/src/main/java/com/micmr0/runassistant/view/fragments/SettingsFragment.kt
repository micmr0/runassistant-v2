package com.micmr0.runassistant.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.micmr0.runassistant.R
import android.widget.EditText
import android.widget.SeekBar
import com.micmr0.runassistant.util.SharedPreferencesHelper
import kotlinx.android.synthetic.main.fragment_settings.*

class Settings : Fragment() {
    private lateinit var mWeightContainer: View
    private lateinit var mLocationPrecisionSeekBar: SeekBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mWeightContainer = fragment_settings_weight_container
        mLocationPrecisionSeekBar = fragment_settings_location_precision_seek_baar

        mWeightContainer.setOnClickListener {
            val li = LayoutInflater.from(activity)
            val dialogView = li.inflate(R.layout.weight_dialog, null)
            val alertDialogBuilder = AlertDialog.Builder(activity)

            alertDialogBuilder.setTitle(getString(R.string.set_weight_title))
            alertDialogBuilder.setView(dialogView)
            val userInput = dialogView.findViewById(R.id.weight_edit_text) as EditText
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        SharedPreferencesHelper.setWeight(Integer.valueOf(userInput.text.toString()))
                    }
                    .setNegativeButton(getString(R.string.training_end_cancel)) { dialog, _ -> dialog.cancel() }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        val locationPrecision = SharedPreferencesHelper.getLocationPrecision()
        when (locationPrecision) {
            LocationPrecision.LOW.value -> mLocationPrecisionSeekBar.progress = 0
            LocationPrecision.NORMAL.value -> mLocationPrecisionSeekBar.progress = 1
            LocationPrecision.HIGH.value -> mLocationPrecisionSeekBar.progress = 2
        }

        mLocationPrecisionSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, pProgress: Int, p2: Boolean) {
                when (pProgress) {
                    0 -> SharedPreferencesHelper.setLocationPrecision(LocationPrecision.LOW.value)
                    1 -> SharedPreferencesHelper.setLocationPrecision(LocationPrecision.NORMAL.value)
                    2 -> SharedPreferencesHelper.setLocationPrecision(LocationPrecision.HIGH.value)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    enum class LocationPrecision(pLocationPrecision: Long) {
        LOW(8000L), NORMAL(5000L), HIGH(2000L);
        val value = pLocationPrecision
    }
}