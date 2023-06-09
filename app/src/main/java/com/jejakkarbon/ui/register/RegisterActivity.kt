package com.jejakkarbon.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.jejakkarbon.R
import com.jejakkarbon.databinding.ActivityRegisterBinding
import com.jejakkarbon.di.Injection
import com.jejakkarbon.model.RegisterRequest
import com.jejakkarbon.model.UserToken
import com.jejakkarbon.preferences.Preferences
import com.jejakkarbon.ui.dashboard.DashboardActivity
import com.jejakkarbon.ui.login.LoginActivity
import com.jejakkarbon.ui.onboarding.OnboardingActivity
import com.jejakkarbon.utils.Result

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeDependencies()
        setupClickListeners()
    }

    private fun initializeDependencies() {
        val applicationContext = applicationContext
        Injection.initialize(applicationContext)
        registerViewModel = Injection.provideRegisterViewModel(this)
        preferences = Preferences(this)
        googleSignInClient = createGoogleSignInClient()
    }

    private fun setupClickListeners() {
        binding.btRegister.setOnClickListener {
            performRegister()
        }

        binding.ivGoogleRegisLogin.setOnClickListener {
            signInWithGoogle()
        }

        binding.tvAlreadyHaveAcc.setOnClickListener {
            navigateToLoginActivity()
        }
    }

    private fun observeRegisterUserState() {
        registerViewModel.registerState.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Registration successful")
                    navigateToLoginActivity()
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(result.message)
                }

                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun observeRegisterGoogleState() {
        registerViewModel.registerWithGoogleState.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Registration successful")
                    checkUserLoginStatus()
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(result.message)
                }

                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    handleGoogleSignInResult(data)
                }
            }
        }

    private fun createGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        return GoogleSignIn.getClient(this, gso)
    }

    private fun handleGoogleSignInResult(data: Intent) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            idToken?.let {
                registerViewModel.registerWithGoogle(it)
                saveUserToken(it)
            }
        } catch (e: ApiException) {
            showToast("Google sign-in failed: ${e.message}")
        }
    }

    private fun saveUserToken(token: String) {
        val userToken = UserToken(token = token, isLogin = true)
        preferences.setToken(userToken)
    }

    private fun checkUserLoginStatus() {
        val userToken by lazy { preferences.getToken() }
        if (userToken.isLogin) {
            if (userToken.isFirstLogin) {
                navigateToOnboardingActivity()
            } else {
                navigateToDashboardActivity()
            }
        }
    }

    private fun performRegister() {
        val email = binding.edEmailRegister.text.toString()
        val password = binding.edPasswordRegister.text.toString()
        val displayName = binding.edUsernameRegister.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && displayName.isNotEmpty()) {
            val userData = RegisterRequest(email, password, displayName)
            registerViewModel.registerUser(userData)
            observeRegisterUserState()
        } else {
            showToast("Please fill in all the fields.")
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
        observeRegisterGoogleState()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToOnboardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
