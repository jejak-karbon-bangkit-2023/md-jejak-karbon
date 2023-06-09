package com.jejakkarbon.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jejakkarbon.R
import com.jejakkarbon.databinding.ActivityDashboardBinding
import com.jejakkarbon.di.Injection
import com.jejakkarbon.model.Guide
import com.jejakkarbon.ui.guide.GuideDetailActivity
import com.jejakkarbon.utils.Result

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var progressContainer: LinearLayout
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var guideAdapter: GuideAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressContainer = binding.ChartContainer
        val applicationContext = applicationContext
        Injection.initialize(applicationContext)
        dashboardViewModel = Injection.provideDashboardViewModel(this)

        val data1 = 250
        val data2 = 150
        val total = data1 + data2
        val data1Percentage = (data1.toFloat() / total) * 100
        val data2Percentage = (data2.toFloat() / total) * 100

        createDonutProgress(data1Percentage, data2Percentage)


        showRecyclerView()
    }

    private fun createDonutProgress(data1Percentage: Float, data2Percentage: Float) {
        val progressView = DonutProgressView(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )

        progressView.layoutParams = layoutParams
        progressView.setProgress(data1Percentage, data2Percentage)
        progressContainer.addView(progressView)
    }

    private inner class DonutProgressView(context: Context) : View(context) {
        val boldTypeface = ResourcesCompat.getFont(context, R.font.quicksand_bold)
        val lightTypeface = ResourcesCompat.getFont(context, R.font.quicksand_light)
        private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        private var data1Percentage: Float = 0f
        private var data2Percentage: Float = 0f
        private val firstLineText = "249 Kg"
        private val secondLineText = "CO2 emitted"

        init {
            progressPaint.color = ContextCompat.getColor(context, R.color.md_theme_dark_primary)
            backgroundPaint.color = ContextCompat.getColor(context, R.color.md_theme_light_primary)
            textPaint.color = Color.BLACK
            textPaint.textSize = resources.getDimension(R.dimen.center_text_size)
            textPaint.textAlign = Paint.Align.CENTER
        }

        fun setProgress(data1Percentage: Float, data2Percentage: Float) {
            this.data1Percentage = data1Percentage
            this.data2Percentage = data2Percentage
            invalidate()
        }

        @SuppressLint("DrawAllocation")
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val width = width.toFloat()
            val height = height.toFloat()
            val centerX = width * 0.5f
            val centerY = height * 0.45f
            val radius = width.coerceAtMost(height) * 0.4f
            val strokeWidth = radius * 0.3f

            // Draw the background circle
            val backgroundRadius = radius - strokeWidth * 0.5f
            backgroundPaint.strokeWidth = strokeWidth
            backgroundPaint.style = Paint.Style.STROKE
            canvas.drawCircle(centerX, centerY, backgroundRadius, backgroundPaint)

            // Draw the data1 progress arc
            val data1StartAngle = -90f
            val data1SweepAngle = ((data1Percentage / 100f) * 360f).coerceAtMost(360f)
            progressPaint.strokeWidth = strokeWidth
            progressPaint.style = Paint.Style.STROKE
            canvas.drawArc(
                centerX - backgroundRadius,
                centerY - backgroundRadius,
                centerX + backgroundRadius,
                centerY + backgroundRadius,
                data1StartAngle,
                data1SweepAngle,
                false,
                progressPaint
            )

            // Draw the data2 progress arc
            val data2StartAngle = data1StartAngle + data1SweepAngle
            val data2SweepAngle = ((data2Percentage / 100f) * 360f).coerceAtMost(360f)
            progressPaint.color = Color.RED
            canvas.drawArc(
                centerX - backgroundRadius,
                centerY - backgroundRadius,
                centerX + backgroundRadius,
                centerY + backgroundRadius,
                data2StartAngle,
                data2SweepAngle,
                false,
                progressPaint
            )

            // Draw the center text
            val firstLineTextSize = resources.getDimension(R.dimen.first_line_text)
            val secondLineTextSize = resources.getDimension(R.dimen.second_line_text)

            val firstLineTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                typeface = boldTypeface
                color = Color.BLACK
                textSize = firstLineTextSize
                textAlign = Paint.Align.CENTER
            }

            val secondLineTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                typeface = lightTypeface
                color = Color.BLACK
                textSize = secondLineTextSize
                textAlign = Paint.Align.CENTER
            }

            val firstLineTextRect = Rect()
            firstLineTextPaint.getTextBounds(
                firstLineText, 0, firstLineText.length, firstLineTextRect
            )
            val firstLineTextY = centerY - firstLineTextRect.height() * 0.5f
            canvas.drawText(firstLineText, centerX, firstLineTextY, firstLineTextPaint)

            val secondLineTextRect = Rect()
            secondLineTextPaint.getTextBounds(
                secondLineText, 0, secondLineText.length, secondLineTextRect
            )
            val secondLineTextY =
                centerY + secondLineTextRect.height() * 0.5f + firstLineTextRect.height() * 0.5f
            canvas.drawText(secondLineText, centerX, secondLineTextY, secondLineTextPaint)
        }
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvGuide.layoutManager = layoutManager
        binding.rvGuide.setHasFixedSize(true)

        dashboardViewModel.guideLiveData.observe(this) { result ->
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

                }

                is Result.Error -> {
                    val errorMessage = result.message
                    // Handle error case
                }

                is Result.Loading -> {
                    // Handle loading case
                }
            }
        }
    }


    private fun goToDetailUser(guide: Guide) {
        val moveToDetail = Intent(this@DashboardActivity, GuideDetailActivity::class.java)
        moveToDetail.putExtra(GuideDetailActivity.EXTRA_ID, guide.id)
        startActivity(moveToDetail)
    }
}