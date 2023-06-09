package com.jejakkarbon.ui.guide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jejakkarbon.R

class GuideDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_detail)
    }

    companion object {
        const val EXTRA_ID = "extra_ID"

    }
}