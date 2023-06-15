package com.jejakkarbon.ui.onboarding

import android.content.Intent
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.jejakkarbon.R
import com.jejakkarbon.databinding.ActivityOnboardingPlantBinding
import com.jejakkarbon.di.Injection
import com.jejakkarbon.preferences.Preferences
import com.jejakkarbon.ui.dashboard.DashboardActivity


class OnboardingPlantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingPlantBinding
    private lateinit var videoTextureView: TextureView
    private lateinit var radioGroup: RadioGroup
    private lateinit var continueBtn: Button
    private lateinit var preferences: Preferences
    private lateinit var onBoardingPlantViewModel: OnBoardingPlantViewModel
    private var ride: String? = null
    private var distance: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = Preferences(this)
        initializeViews()
        setupRadioGroup()
        setupContinueButton()
        setupMediaPlayer()


        val applicationContext = applicationContext
        Injection.initialize(applicationContext)
        onBoardingPlantViewModel = Injection.provideOnBoardingPlantViewModel(this)

        val rideDistance = preferences.getRideAndDistance()

        ride = rideDistance.first
        distance = rideDistance.second
    }

    private fun setupMediaPlayer() {
        val mediaPlayer = MediaPlayer()
        videoTextureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture, width: Int, height: Int
            ) {
                mediaPlayer.setDataSource(
                    applicationContext,
                    Uri.parse("android.resource://com.jejakkarbon/${R.raw.plant}")
                )
                mediaPlayer.setSurface(Surface(surface))
                mediaPlayer.isLooping = true
                mediaPlayer.prepare()
                mediaPlayer.start()

                mediaPlayer.playbackParams = mediaPlayer.playbackParams.apply {
                    speed = 0.5f
                }
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture, width: Int, height: Int
            ) {
                val videoWidth = mediaPlayer.videoWidth
                val videoHeight = mediaPlayer.videoHeight
                if (videoWidth != 0 && videoHeight != 0) {
                    val viewAspectRatio = width.toFloat() / height
                    val videoAspectRatio = videoWidth.toFloat() / videoHeight
                    val scaleX: Float
                    val scaleY: Float
                    if (videoAspectRatio > viewAspectRatio) {
                        scaleX = viewAspectRatio / videoAspectRatio
                        scaleY = 1f
                    } else {
                        scaleX = 1f
                        scaleY = videoAspectRatio / viewAspectRatio
                    }
                    videoTextureView.setTransform(Matrix().apply {
                        setScale(scaleX, scaleY, width / 2f, height / 2f)
                    })
                }
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }
        }
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
            val userToken = preferences.getToken()
            userToken.isFirstLogin = false
            preferences.setToken(userToken)
            onBoardingPlantViewModel.predict(file = null, distance, ride)
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