package com.micmr0.runassistant.util

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.micmr0.runassistant.R
import android.content.pm.FeatureInfo



class GooglePlayServicesHelper {
    companion object {
        private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

        public fun checkGooglePlayServices(pContext: Activity): Boolean {
            val checker = GoogleApiAvailability.getInstance()

            val status = checker.isGooglePlayServicesAvailable(pContext)

            if (status == ConnectionResult.SUCCESS) {
                if (getVersionFromPackageManager(pContext) >= 2) {
                    return true
                } else {
                    Toast.makeText(pContext, R.string.no_maps, Toast.LENGTH_LONG).show()
                    return false
                }
            } else if (checker.isUserResolvableError(status)) {
                checker.getErrorDialog(pContext, status,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show()
                return false
            } else {
                Toast.makeText(pContext, R.string.no_maps, Toast.LENGTH_LONG).show()
                return false
            }
        }

        /*
       * Copyright (C) 2010 The Android Open Source Project
       *
       * Licensed under the Apache License, Version 2.0 (the
       * "License"); you may not use this file except in
       * compliance with the License. You may obtain a copy of
       * the License at
       *
       * http://www.apache.org/licenses/LICENSE-2.0
       *
       * Unless required by applicable law or agreed to in
       * writing, software distributed under the License is
       * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
       * CONDITIONS OF ANY KIND, either express or implied. See
       * the License for the specific language governing
       * permissions and limitations under the License.
       */

        private fun getVersionFromPackageManager(pContext: Context): Int {
            val packageManager = pContext.getPackageManager()
            val featureInfos = packageManager.getSystemAvailableFeatures()
            if (featureInfos != null && featureInfos!!.size > 0) {
                for (featureInfo in featureInfos!!) {
                    // Null feature mName means this feature is the open
                    // gl es version feature.
                    if (featureInfo.name == null) {
                        return if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
                            getMajorVersion(featureInfo.reqGlEsVersion)
                        } else {
                            1 // Lack of property means OpenGL ES
                            // version 1
                        }
                    }
                }
            }
            return 1
        }

        /** @see FeatureInfo.getGlEsVersion
         */
        private fun getMajorVersion(glEsVersion: Int): Int {
            return glEsVersion and -0x10000 shr 16
        }
    }
}