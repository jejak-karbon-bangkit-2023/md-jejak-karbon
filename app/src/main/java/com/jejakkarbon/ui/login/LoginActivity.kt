package com.jejakkarbon.ui.login

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
import com.google.firebase.auth.FirebaseAuth
import com.jejakkarbon.R
import com.jejakkarbon.databinding.ActivityLoginBinding
import com.jejakkarbon.di.Injection
import com.jejakkarbon.model.UserToken
import com.jejakkarbon.preferences.Preferences
import com.jejakkarbon.ui.dashboard.DashboardActivity
import com.jejakkarbon.ui.onboarding.OnboardingActivity
import com.jejakkarbon.ui.register.RegisterActivity
import com.jejakkarbon.utils.Result

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeDependencies()
        setupClickListeners()
        checkUserLoginStatus()
    }

    private fun initializeDependencies() {
        preferences = Preferences(this)
        Injection.initialize(applicationContext)
        loginViewModel = Injection.provideLoginViewModel(this)
        googleSignInClient = createGoogleSignInClient()
    }

    private fun setupClickListeners() {
        binding.ivGoogleLogin.setOnClickListener { signInWithGoogle() }
        binding.btLogin.setOnClickListener { performLogin() }
        binding.tvAlreadyHaveAcc.setOnClickListener { navigateToRegisterActivity() }
    }

    private fun observeLoginState() {
        loginViewModel.loginState.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    checkUserLoginStatus()
                    saveUserToken()
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Login failed: ${result.message}")
                }

                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun saveUserToken() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result.token
                val userToken = UserToken(token = idToken.toString(), isLogin = true)
                preferences.setToken(userToken)
            } else {
                val exception = task.exception
                showToast("Token retrieval failed: ${exception?.message ?: "Token retrieval failed"}")
            }
        }
    }

    private fun checkUserLoginStatus() {
        val userToken by lazy { preferences.getToken() }
        val isFirstLogin = preferences.isFirstLogin()
        if (userToken.isLogin) {
            if (isFirstLogin) {
                navigateToOnboardingActivity()
            } else {
                navigateToDashboardActivity()
            }
        }
    }


    private fun performLogin() {
        val email = binding.edEmailLogin.text.toString()
        val password = binding.edPasswordLogin.text.toString()
        loginViewModel.login(email, password)
        observeLoginState()
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
                loginViewModel.loginWithGoogle(it)
                val isFirstLogin = preferences.isFirstLogin()
                if (isFirstLogin) {
                    navigateToOnboardingActivity()
                } else {
                    navigateToDashboardActivity()
                }
                saveUserToken()
            }
        } catch (e: ApiException) {
            showToast("Google sign-in failed: ${e.message}")
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToOnboardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
