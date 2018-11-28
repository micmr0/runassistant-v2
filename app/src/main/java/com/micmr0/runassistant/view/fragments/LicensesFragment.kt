package com.micmr0.runassistant.view.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.micmr0.runassistant.R
import com.micmr0.runassistant.util.Logger

class LicensesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.d(this, "onCreateView")
        return inflater.inflate(R.layout.fragment_licenses, container, false)
    }

}