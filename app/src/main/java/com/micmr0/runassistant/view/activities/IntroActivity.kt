package com.micmr0.runassistant.view.activities

import android.content.Intent
import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntroFragment
import com.micmr0.runassistant.R
import com.micmr0.runassistant.util.SharedPreferencesHelper

class IntroActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(AppIntroFragment.newInstance(getString(R.string.intro_page_1_title), getString(R.string.intro_page_1_description), R.drawable.training_screen, resources.getColor(R.color.ColorPrimaryDark)))
        addSlide(AppIntroFragment.newInstance(getString(R.string.intro_page_2_title), getString(R.string.intro_page_2_description), R.drawable.summary_screen, resources.getColor(R.color.ColorPrimaryDark)))
        addSlide(AppIntroFragment.newInstance(getString(R.string.intro_page_3_title), getString(R.string.intro_page_3_description), R.drawable.plans_screen, resources.getColor(R.color.ColorPrimaryDark)))
        addSlide(AppIntroFragment.newInstance(getString(R.string.intro_page_4_title), getString(R.string.intro_page_4_description), R.drawable.achievements_screen, resources.getColor(R.color.ColorPrimaryDark)))

        SharedPreferencesHelper.setFirstRun(false)

        skipButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        doneButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}