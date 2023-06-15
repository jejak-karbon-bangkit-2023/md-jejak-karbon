package com.jejakkarbon.ui.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.jejakkarbon.databinding.ActivityPredictBinding
import com.jejakkarbon.di.Injection
import com.jejakkarbon.preferences.Preferences
import com.jejakkarbon.ui.dashboard.DashboardActivity
import com.jejakkarbon.utils.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PredictActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: ActivityPredictBinding
    private lateinit var predictViewModel: PredictViewModel
    private lateinit var preferences: Preferences
    private var ride: String? = null
    private var distance: Int? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val applicationContext = applicationContext
        Injection.initialize(applicationContext)
        predictViewModel = Injection.providePredictViewModel(this)
        progressBar = binding.progressBar
        preferences = Preferences(this)
        val rideDistance = preferences.getRideAndDistance()

        ride = rideDistance.first
        distance = rideDistance.second
        val imageUriString: String? = intent.getStringExtra("imageUri")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            val imageFile = imageUri.path?.let { File(it) }
            if (imageFile != null) {
                predictImage(imageFile, distance, ride)
            }
        }


        binding.btnContinue.setOnClickListener {
            navigateToDashboardActivity()
        }
    }

    private fun navigateToDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        val userToken = preferences.getToken()
        userToken.isFirstLogin = false
        preferences.setToken(userToken)
        finish()
    }

    private fun predictImage(image: File, distance: Int?, transport: String?) {
        val requestFile = image.asRequestBody("image/*".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("file", image.name, requestFile)
        predictViewModel.predict(multipartBody, distance, transport)
        predictObserve()
    }

    private fun predictObserve() {
        predictViewModel.predictResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    val predictResponse = result.data
                    val lastPlant = predictResponse?.data_plant?.plant?.lastOrNull()
                    val plantName = lastPlant?.name
                    val plantImage = lastPlant?.image_url
                    binding.tvTitlePlant.text = plantName
                    if (!plantImage.isNullOrEmpty()) {
                        Glide.with(this).load(plantImage).into(binding.ivPlantPreview)
                    }
                    binding.tvTitlePlant.visibility = View.VISIBLE
                    binding.cardView.visibility = View.VISIBLE
                    binding.tvPlantPreview.visibility = View.VISIBLE
                    binding.tvTitlePlantPredicted.visibility = View.VISIBLE
                    binding.btnContinue.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    val errorMessage = result.message
                    Toast.makeText(this, "Prediction failed: $errorMessage", Toast.LENGTH_SHORT)
                        .show()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}

