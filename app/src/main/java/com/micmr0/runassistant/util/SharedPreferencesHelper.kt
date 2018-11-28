package com.micmr0.runassistant.util

import android.content.Context
import android.content.Context.MODE_MULTI_PROCESS
import android.content.SharedPreferences
import com.micmr0.runassistant.RAApplication


private val SHARED_PREFERENCES_NAME = "gja_shared_preferences"

private val FIRST_RUN = "first_run"
private val WEIGHT = "weight"
private val LOCATION_PRECISION = "location_precision"
private val ACTIVITY_TYPE = "activity_type"

class SharedPreferencesHelper {
    companion object {
        fun getSharedPreferences(): SharedPreferences {
            return RAApplication.sInstance!!.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_MULTI_PROCESS)
        }

        fun isFirstRun(): Boolean {
            return getBoolean(FIRST_RUN, true)
        }

        fun setFirstRun(pFirstRun: Boolean) {
            editBoolean(FIRST_RUN, pFirstRun)
        }

        fun getLocationPrecision(): Long? {
            return getLong(LOCATION_PRECISION, 5000L)
        }

        fun setLocationPrecision(pLocationPrecision : Long) {
            editLong(LOCATION_PRECISION, pLocationPrecision)
        }

        fun getWeight(): Int? {
            return getInteger(WEIGHT, 0)
        }

        fun setWeight(pWeight : Int) {
            editInteger(WEIGHT, pWeight)
        }

        fun getActivityType(): Int? {
            return getInteger(ACTIVITY_TYPE, 0)
        }

        fun setActivityType(pActivityType : Int) {
            editInteger(ACTIVITY_TYPE, pActivityType)
        }


        fun getString(pName: String): String {
            return getSharedPreferences().getString(pName, "")
        }

        fun getString(pName: String, pDefaultValue: String?): String? {
            return getSharedPreferences().getString(pName, pDefaultValue)
        }

        fun getBoolean(pName: String, pDefaultValue: Boolean): Boolean {
            return getSharedPreferences().getBoolean(pName, pDefaultValue)
        }

        fun getInteger(pName: String, pDefaultValue: Int): Int {
            return getSharedPreferences().getInt(pName, pDefaultValue)
        }

        fun getLong(pName: String, pDefaultValue: Long): Long {
            return getSharedPreferences().getLong(pName, pDefaultValue)
        }

        fun editString(pName: String, pValue: String) {
            getSharedPreferences().edit().putString(pName, pValue).commit()
        }

        fun editBoolean(pName: String, pValue: Boolean) {
            getSharedPreferences().edit().putBoolean(pName, pValue).commit()
        }

        fun editInteger(pName: String, pValue: Int) {
            getSharedPreferences().edit().putInt(pName, pValue).commit()
        }

        fun editLong(pName: String, pValue: Long) {
            getSharedPreferences().edit().putLong(pName, pValue).commit()
        }

    }
}

