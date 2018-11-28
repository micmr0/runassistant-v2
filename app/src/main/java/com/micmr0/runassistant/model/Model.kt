package com.micmr0.runassistant.model

import android.content.Context
import com.micmr0.runassistant.model.loaders.LocationLoader

class Model(pContext : Context) {
    internal var mLocationLoader : LocationLoader = LocationLoader(pContext)
}