package com.jejakkarbon.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.jejakkarbon.R
import com.jejakkarbon.adapter.GuideAdapter
import com.jejakkarbon.databinding.ActivityDashboardBinding
import com.jejakkarbon.di.Injection
import com.jejakkarbon.model.Guide
import com.jejakkarbon.ui.guide.GuideActivity
import com.jejakkarbon.ui.guide.GuideDetailActivity
import com.jejakkarbon.ui.onboarding.CameraActivity
import com.jejakkarbon.ui.profile.ProfileActivity
import com.jejakkarbon.utils.Result

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var progressContainer: LinearLayout
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var guideAdapter: GuideAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvFootprintResult: TextView
    private lateinit var tvImpactResult: TextView
    private lateinit var donutProgressView: DonutProgressView
    private lateinit var bottomNavigationView: BottomNavigationView
    private var userId: String? = null
    private var textKarbon: String = "0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()
        val applicationContext = applicationContext
        Injection.initialize(applicationContext)
        dashboardViewModel = Injection.provideDashboardViewModel(this)

        bottomNavigationView = binding.navView
        bottomNavigationView.selectedItemId = R.id.nav_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {

                    true
                }
                R.id.nav_statistics ->{
                   showToast("Feature not ready yet")
                    false
                }

                R.id.nav_add -> {
                    startActivity(Intent(getApplicationContext(), CameraActivity::class.java))
                    overridePendingTransition(0, 0)
                    false
                }

                R.id.nav_guide -> {
                    startActivity(Intent(getApplicationContext(), GuideActivity::class.java))
                    overridePendingTransition(0, 0)
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


        showRecyclerView()
        val currentUser = FirebaseAuth.getInstance().currentUser
        userId = currentUser?.uid
        Log.d("userid", userId.toString())
        dashboardViewModel.getGuide()
        dashboardViewModel.getUserDetail(userId.toString())
        observeUserData()
    }

    private fun initializeViews() {
        tvFootprintResult = binding.tvFootprintResult
        tvImpactResult = binding.tvImpactResult
        progressContainer = binding.ChartContainer
        progressBar = binding.progressBar
        donutProgressView = DonutProgressView(this)
        progressContainer.addView(donutProgressView)
    }

    private fun createDonutProgress(data1Percentage: Float, data2Percentage: Float) {
        donutProgressView.setProgress(data1Percentage, data2Percentage)
    }

    private inner class DonutProgressView(context: Context) : View(context) {
        private val boldTypeface = ResourcesCompat.getFont(context, R.font.quicksand_bold)
        private val lightTypeface = ResourcesCompat.getFont(context, R.font.quicksand_light)
        private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        private var data1Percentage: Float = 0f
        private var data2Percentage: Float = 0f
        private val firstLineText: String
            get() = textKarbon
        private val secondLineText = "CO2 emitted"

        private var progressColor1: Int = ContextCompat.getColor(
            context, R.color.md_theme_light_primary
        ) // Change to desired color
        private var progressColor2: Int =
            ContextCompat.getColor(context, R.color.md_theme_light_error) // Change to desired color

        init {
            progressPaint.color = progressColor1
            backgroundPaint.color = progressColor1
            textPaint.color = Color.BLACK
            textPaint.textSize = resources.getDimension(R.dimen.center_text_size)
            textPaint.textAlign = Paint.Align.CENTER
        }

        fun setProgress(data1Percentage: Float, data2Percentage: Float) {
            this.data1Percentage = data1Percentage
            this.data2Percentage = data2Percentage

            progressColor1 = if (data1Percentage > 0) {
                ContextCompat.getColor(
                    context, R.color.md_theme_light_primary
                ) // Change to desired color
            } else {
                ContextCompat.getColor(
                    context, R.color.md_theme_light_primary
                ) // Change to desired color
            }

            progressColor2 = if (data2Percentage > 0) {
                ContextCompat.getColor(
                    context, R.color.md_theme_light_error
                ) // Change to desired color
            } else {
                ContextCompat.getColor(
                    context, R.color.md_theme_light_error
                ) // Change to desired color
            }

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
            progressPaint.color = progressColor1
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
            progressPaint.color = progressColor2
            val data2StartAngle = data1StartAngle + data1SweepAngle
            val data2SweepAngle = ((data2Percentage / 100f) * 360f).coerceAtMost(360f)
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

        dashboardViewModel.dashboardLiveData.observe(this) { result ->
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

                    progressBar.visibility = View.GONE
                }

                is Result.Error -> {
                    val errorMessage = result.message
                    showToast("Login failed: $errorMessage")
                    progressBar.visibility = View.GONE
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeUserData() {
        dashboardViewModel.userLiveData.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    val userData = result.data
                    if (userData != null) {
                        val transportData = userData.data.transport
                        var cOutSum = 0

                        val plantData = userData.data.plant
                        var cInSum = 0

                        for (transport in transportData) {
                            val cOut = transport.c_out
                            cOutSum += cOut.toInt()
                        }
                        for (plant in plantData) {
                            val cIn = plant.c_in
                            cInSum += cIn.toInt()
                        }

                        val total = cInSum + cOutSum
                        val data1Percentage = (cInSum.toFloat() / total) * 100
                        val data2Percentage = (cOutSum.toFloat() / total) * 100
                        createDonutProgress(data1Percentage, data2Percentage)

                        tvImpactResult.text = "$cInSum Kg"
                        tvFootprintResult.text = "$cOutSum Kg"
                        textKarbon = "$cOutSum Kg"
                        val mergedString = userData.data.name + " " + userData.data.status

                        val spannableString = SpannableString(mergedString)
                        val statusStartIndex = userData.data.name.length + 1

                        val greenColor =
                            ContextCompat.getColor(this, R.color.md_theme_light_primary)
                        val redColor = ContextCompat.getColor(this, R.color.md_theme_light_error)

                        val colorSpan = if (userData.data.status == "Manusia Hijau") {
                            ForegroundColorSpan(greenColor)
                        } else {
                            ForegroundColorSpan(redColor)
                        }

                        spannableString.setSpan(
                            colorSpan,
                            statusStartIndex,
                            mergedString.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                        binding.tvNameDisplay.text = spannableString
                        donutProgressView.invalidate()
                    }
                    progressBar.visibility = View.GONE
                }

                is Result.Error -> {
                    val errorMessage = result.message
                    showToast("get user data failed: $errorMessage")
                    progressBar.visibility = View.GONE
                }

            }
        }
    }

    private fun goToDetailUser(guide: Guide) {
        val moveToDetail = Intent(this@DashboardActivity, GuideDetailActivity::class.java)
        moveToDetail.putExtra(GuideDetailActivity.EXTRA_ID, guide.id)
        startActivity(moveToDetail)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}