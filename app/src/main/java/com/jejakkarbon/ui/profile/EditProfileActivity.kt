package com.jejakkarbon.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.jejakkarbon.databinding.ActivityEditProfileBinding
import com.jejakkarbon.di.Injection
import com.jejakkarbon.utils.Result
import com.google.firebase.auth.FirebaseAuth

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var editProfileViewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Injection.initialize(applicationContext)
        editProfileViewModel = Injection.provideEditProfileViewModel(this)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val userId: String? = auth.currentUser?.uid

        binding.buttonSave.setOnClickListener {
            val newName = binding.nameEditText.text.toString()
            val newEmail = binding.emailEditText.text.toString()
            if (userId != null) {
                editProfileViewModel.editProfile(userId, newName, newEmail)
            }
        }

        editProfileViewModel.editProfileState.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                }

                is Result.Success -> {
                    Snackbar.make(
                        binding.root, "Profile updated successfully", Snackbar.LENGTH_SHORT
                    ).show()
                    finish()
                }

                is Result.Error -> {
                    // Edit profile failed
                    Snackbar.make(
                        binding.root,
                        result.message,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}