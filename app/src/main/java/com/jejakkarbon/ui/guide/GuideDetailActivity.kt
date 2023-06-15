package com.jejakkarbon.ui.guide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.jejakkarbon.databinding.ActivityGuideDetailBinding
import com.jejakkarbon.di.Injection

import com.jejakkarbon.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GuideDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuideDetailBinding
    private lateinit var guideDetailViewModel: GuideDetailViewModel
    private var guideId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        guideId = intent.getStringExtra(EXTRA_ID)
        val applicationContext = applicationContext
        Injection.initialize(applicationContext)
        guideDetailViewModel = Injection.provideguideDetailViewModel(this)
        val coroutineScope = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch {
            guideId?.let { guideDetailViewModel.getDetailGuide(it) }

        }



        guideDetailViewModel.guideDetailLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val guide = result.data
                    Glide.with(this).load(guide?.image_url).into(binding.imageGuide)
                    binding.textTitle.text = guide?.title
                    binding.textDescription.text = guide?.title
                    val paragraphsBuilder = StringBuilder()
                    for (paragraph in guide?.paragraphs!!) {
                        paragraphsBuilder.append(paragraph).append("\n\n")
                    }
                    binding.textDescription.text = paragraphsBuilder.toString()

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


    companion object {
        const val EXTRA_ID = "extra_ID"
    }
}