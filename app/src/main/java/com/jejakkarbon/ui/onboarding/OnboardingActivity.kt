package com.jejakkarbon.ui.onboarding

import android.app.AlertDialog
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.jejakkarbon.R

class OnboardingActivity : AppCompatActivity() {
    private lateinit var videoTextureView: TextureView
    private lateinit var radioGroup: RadioGroup
    private lateinit var customRadioValue: RadioButton
    private var previousCustomValue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        videoTextureView = findViewById(R.id.videoTextureView)
        radioGroup = findViewById(R.id.radioGroup)
        customRadioValue = findViewById(R.id.radiobtn_5)

        videoTextureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.traffic)
                mediaPlayer.setSurface(Surface(surface))
                mediaPlayer.isLooping = true
                mediaPlayer.start()
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
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




