package com.example.tohwangsoi_mobile

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tohwangsoi_mobile.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val db = FirebaseFirestore.getInstance()

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupViews()
        initGoogleSignIn()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // ถ้าเคย Login แล้ว ให้ไปหน้า MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupViews() {
        binding.btnLogin.setOnClickListener {
            if (validateInputs()) {
                performLogin()
            }
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        binding.btnGoogleLogin.setOnClickListener {
            performGoogleLogin()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Please enter a valid email"
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        val password = binding.etPassword.text.toString()
        if (password.isEmpty()) {
            binding.tilPassword.error = "Please enter your password"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        return isValid
    }

    // checklogin
    private fun performLogin() {
        binding.btnLogin.isEnabled = false
        binding.btnLogin.text = "Logging in..."
        binding.progressBar.visibility = View.VISIBLE

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()


        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val storedPassword = document.getString("password")

                    if (storedPassword == password) {
                        //
                        val role = document.getString("role")
                        if (role == "customer") {
                            navigateToMainActivity()
                        } else if (role == "manager") {
                            navigateToManagerActivity()
                        } else {
                            Toast.makeText(this, "Unknown role", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // ถ้ารหัสผ่านไม่ตรง
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "No such user found", Toast.LENGTH_SHORT).show()
                }
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Login"
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching user", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Login"
            }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToManagerActivity() {
        startActivity(Intent(this, HomeManager::class.java))
        finish()
    }

    // Login with google

    private fun initGoogleSignIn() {
        googleSignInClient = GoogleSignIn.getClient(
            this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }

    private fun performGoogleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Google login successful!", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
