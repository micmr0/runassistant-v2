package com.micmr0.runassistant.view.fragments

import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.micmr0.runassistant.BuildConfig
import com.micmr0.runassistant.R
import com.micmr0.runassistant.util.Logger
import com.micmr0.runassistant.view.OnFragmentInteractionListener

class AppInfoFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit  var mLicensesButton : Button
    private lateinit  var mPrivacyPolicyButton : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.d(this, "onCreateView")
        return inflater.inflate(R.layout.fragment_app_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view.findViewById(R.id.fragment_app_info_version) as TextView).text = BuildConfig.VERSION_NAME

        mLicensesButton = view.findViewById(R.id.fragment_app_info_licenses)
        mPrivacyPolicyButton = view.findViewById(R.id.fragment_app_info_policy_privacy)

        mLicensesButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.activity_main_container, LicensesFragment()).addToBackStack(null).commit()
        }

        mPrivacyPolicyButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy_url)))
            startActivity(intent)
        }
    }
}