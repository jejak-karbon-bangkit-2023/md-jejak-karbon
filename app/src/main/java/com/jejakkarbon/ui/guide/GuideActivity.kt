package com.jejakkarbon.ui.guide

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jejakkarbon.R
import com.jejakkarbon.adapter.GuideAdapter
import com.jejakkarbon.databinding.ActivityGuideBinding
import com.jejakkarbon.di.Injection
import com.jejakkarbon.model.Guide
import com.jejakkarbon.ui.dashboard.DashboardActivity
import com.jejakkarbon.ui.profile.ProfileActivity
import com.jejakkarbon.utils.Result
import kotlinx.coroutines.launch

class GuideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuideBinding
    private lateinit var guideViewModel: GuideViewModel
    private lateinit var guideAdapter: GuideAdapter
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val applicationContext = applicationContext
        Injection.initialize(applicationContext)
        guideViewModel = Injection.provideguideViewModel(this)

        lifecycleScope.launch {
            guideViewModel.getGuide()
        }
//        setupBottomNavigation()
        showRecyclerView()
        bottomNavigationView = binding.navView
        bottomNavigationView.selectedItemId = R.id.nav_guide

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(getApplicationContext(), DashboardActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.nav_statistics ->{
                    showToast()
                    false
                }

                R.id.nav_guide -> {

                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(getApplicationContext(), ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                // Add more cases for other items in your navigation menu
                else -> false
            }
        }
    }


    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvGuide.layoutManager = layoutManager
        binding.rvGuide.setHasFixedSize(true)

        guideViewModel.guideLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val guide = result.data
                    guideAdapter = guide?.let { GuideAdapter(it) }!!
                    binding.rvGuide.adapter = guideAdapter
                    guideAdapter.setOnItemClickCallback(object : GuideAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: Guide) {
                            goToDetailUser(data)
                        }
                    })

                    binding.progressBar.visibility = View.GONE
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }

                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun goToDetailUser(guide: Guide) {
        val moveToDetail = Intent(this@GuideActivity, GuideDetailActivity::class.java)
        moveToDetail.putExtra(GuideDetailActivity.EXTRA_ID, guide.id)
        startActivity(moveToDetail)
    }
    private fun showToast() {
        Toast.makeText(this, "Feature not ready yet", Toast.LENGTH_SHORT).show()
    }
}