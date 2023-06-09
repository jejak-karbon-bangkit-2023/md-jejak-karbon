package com.jejakkarbon.ui.onboarding

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.jejakkarbon.R
import com.jejakkarbon.preferences.Preferences
import com.jejakkarbon.ui.dashboard.DashboardActivity

class OptionsDialogFragment : DialogFragment() {
    private lateinit var preferences: Preferences

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val dimAmount = 0.5f // Adjust the value as desired
        dialog.window?.setDimAmount(dimAmount)

        var layoutParams = WindowManager.LayoutParams().apply {
            copyFrom(dialog.window?.attributes)
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }
        dialog.window?.attributes = layoutParams

        val dialogLayout = LinearLayout(requireContext()).apply {
            layoutParams = WindowManager.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(16, 0, 16, 0) // Adjust the margin values as desired
        }

        val contentView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_options, dialogLayout, false)

        dialogLayout.addView(contentView)

        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)

        val btnSkip = contentView.findViewById<Button>(R.id.btnSkip)
        val btnContinue = contentView.findViewById<Button>(R.id.btnContinue)

        btnSkip.setOnClickListener {
            handleSkipOption()
            val userToken = preferences.getToken()
            userToken.isFirstLogin = false
            preferences.setToken(userToken)
            dismiss()
        }

        btnContinue.setOnClickListener {
            startCameraActivity()
            dismiss()
        }

        dialog.setContentView(dialogLayout)

        return dialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = Preferences(context)
    }

    private fun handleSkipOption() {
        val intent = Intent(requireContext(), DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun startCameraActivity() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        startActivity(intent)
    }
}