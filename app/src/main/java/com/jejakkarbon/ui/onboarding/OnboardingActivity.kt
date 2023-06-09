package com.jejakkarbon.ui.onboarding

import android.app.AlertDialog
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
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.jejakkarbon.R
import com.jejakkarbon.databinding.ActivityOnboardingBinding
import com.jejakkarbon.preferences.Preferences

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var videoTextureView: TextureView
    private lateinit var radioGroup: RadioGroup
    private lateinit var preferences: Preferences
    private lateinit var customRadioValue: RadioButton
    private lateinit var continueBtn: Button
    private var previousCustomValue: String = ""
    private var customValue: String = "0"
    private var radioValue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        setupContinueButton()
        setupMediaPlayer()
        setupRadioGroup()
        preferences = Preferences(this)
    }

    private fun initializeViews() {
        videoTextureView = binding.videoTextureView
        radioGroup = binding.radioGroup
        customRadioValue = binding.radiobtn5
        continueBtn = binding.buttonContinue
    }

    private fun setupContinueButton() {
        continueBtn.setOnClickListener {
            if (radioValue != 0) {
                val intent = Intent(this, OnboardingTransport::class.java)
                preferences.setDistance(radioValue)
                startActivity(intent)

            } else {
                val intent = Intent(this, OnboardingPlantActivity::class.java)
                preferences.setDistance(radioValue)
                startActivity(intent)
            }
        }
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
                    Uri.parse("android.resource://com.jejakkarbon/${R.raw.car_traffic}")
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

    private fun setupRadioGroup() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val index = radioGroup.indexOfChild(radioButton)

            val isRadioButtonSelected = index >= 0
            continueBtn.isEnabled = isRadioButtonSelected

            if (index == 4 && radioButton.isChecked) {
                showCustomInputDialog()
            }
            when (index) {
                0 -> {
                    radioValue = 0
                }

                1 -> {
                    radioValue = 100
                }

                2 -> {
                    radioValue = 300
                }

                3 -> {
                    radioValue = 500
                }

                4 -> {
                    radioValue = customValue.toInt()
                }
            }
        }
    }

    private fun showCustomInputDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom_input, null)
        val editTextCustomValue = dialogView.findViewById<EditText>(R.id.editTextCustomValue)

        editTextCustomValue.setText(previousCustomValue)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton("Submit") { dialog, _ ->
            customValue = editTextCustomValue.text.toString()
            handleCustomValue(customValue)
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun handleCustomValue(customValue: String) {
        previousCustomValue = customValue
        customRadioValue.text = buildString {
            append(customValue)
            append(" Kilometers")
        }
    }
}




