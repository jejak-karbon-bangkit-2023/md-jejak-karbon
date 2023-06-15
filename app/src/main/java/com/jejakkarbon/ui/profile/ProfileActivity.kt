package com.jejakkarbon.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.jejakkarbon.R
import com.jejakkarbon.databinding.ActivityProfileBinding
import com.jejakkarbon.preferences.Preferences
import com.jejakkarbon.ui.dashboard.DashboardActivity
import com.jejakkarbon.ui.guide.GuideActivity
import com.jejakkarbon.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var preferences: Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = Preferences(this)
        bottomNavigationView = binding.navView
        bottomNavigationView.selectedItemId = R.id.nav_profile


        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, DashboardActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.nav_statistics -> {
                    showToast()
                    false
                }

                R.id.nav_guide -> {
                    startActivity(Intent(applicationContext, GuideActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.nav_profile -> {

                    true
                }
                // Add more cases for other items in your navigation menu
                else -> false
            }
        }
        binding.Profile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.LogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            preferences.resetToken()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun showToast() {
        Toast.makeText(this, "Feature not ready yet", Toast.LENGTH_SHORT).show()
    }
}