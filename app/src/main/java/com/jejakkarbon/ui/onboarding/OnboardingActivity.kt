package com.jejakkarbon.ui.onboarding

import android.app.AlertDialog
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.jejakkarbon.R
import com.jejakkarbon.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var videoTextureView: TextureView
    private lateinit var radioGroup: RadioGroup
    private lateinit var customRadioValue: RadioButton
    private var previousCustomValue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoTextureView = binding.videoTextureView
        radioGroup = binding.radioGroup
        customRadioValue = binding.radiobtn5

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

                // Set playback speed to 0.5 (half speed)
                mediaPlayer.playbackParams = mediaPlayer.playbackParams.apply {
                    speed = 0.5f
                }
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture, width: Int, height: Int
            ) {
                // Handle size change if needed
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                // Texture is updated
            }
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val index = radioGroup.indexOfChild(radioButton)

            if (index == 4 && radioButton.isChecked) {
                showCustomInputDialog()
            }
        }
    }


    private fun showCustomInputDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom_input, null)
        val editTextCustomValue = dialogView.findViewById<EditText>(R.id.editTextCustomValue)

        // Set the previous custom value if available
        editTextCustomValue.setText(previousCustomValue)

        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton("Submit") { dialog, _ ->
            val customValue = editTextCustomValue.text.toString()
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




