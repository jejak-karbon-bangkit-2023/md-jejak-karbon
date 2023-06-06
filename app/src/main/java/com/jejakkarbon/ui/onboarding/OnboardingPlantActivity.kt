package com.jejakkarbon.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.jejakkarbon.databinding.ActivityOnboardingPlantBinding
import com.jejakkarbon.preferences.Preferences
import com.jejakkarbon.ui.dashboard.DashboardActivity


class OnboardingPlantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingPlantBinding
    private lateinit var videoTextureView: TextureView
    private lateinit var radioGroup: RadioGroup
    private lateinit var continueBtn: Button
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = Preferences(this)

        initializeViews()
        setupRadioGroup()
        setupContinueButton()
    }

    private fun initializeViews() {
        continueBtn = binding.buttonContinue
        videoTextureView = binding.videoTextureView
        radioGroup = binding.radioGroup
    }

    private fun setupRadioGroup() {
        val radioButton2: RadioButton = binding.radiobtn2
        radioButton2.setOnClickListener {
            continueBtn.isEnabled = false
            showPopup()
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val index = radioGroup.indexOfChild(radioButton)
            val isRadioButtonSelected = index >= 0
            continueBtn.isEnabled = isRadioButtonSelected
        }
    }

    private fun setupContinueButton() {
        continueBtn.setOnClickListener {
            navigateToDashboardActivity()
            preferences.setFirstLogin(false)
        }
    }

    private fun showPopup() {
        val fragment = OptionsDialogFragment()
        fragment.show(supportFragmentManager, "OptionsDialog")
    }

    private fun navigateToDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}